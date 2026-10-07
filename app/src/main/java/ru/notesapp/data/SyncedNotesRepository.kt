package ru.notesapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.notesapp.data.local.NoteDao
import ru.notesapp.data.local.NoteEntity
import ru.notesapp.data.local.toDomain
import ru.notesapp.data.remote.NetworkResult
import ru.notesapp.data.remote.NoteApi
import ru.notesapp.data.remote.NoteDto
import ru.notesapp.data.remote.safeApiCall
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import javax.inject.Inject

class SyncedNotesRepository @Inject constructor(
    private val dao: NoteDao,
    private val api: NoteApi,
) : NotesRepository {

    override fun observeAll(): Flow<List<Note>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    override fun search(query: String): Flow<List<Note>> =
        dao.searchByTitle(query).map { list -> list.map { it.toDomain() } }

    override suspend fun getById(id: Long): Note? =
        dao.getById(id)?.toDomain()

    override suspend fun refresh(): NetworkResult<Unit> = safeApiCall {
        val posts = api.listPosts()
        val entities = posts.map { it.toEntity() }
        dao.insertAll(entities)
    }

    override suspend fun add(note: Note): NetworkResult<Long> = safeApiCall {
        val created = api.createPost(NoteDto(title = note.title, body = note.content))
        dao.insert(
            NoteEntity(
                title = note.title,
                content = note.content,
                createdAt = System.currentTimeMillis(),
                type = note.type,
                imageUrl = note.imageUrl,
            )
        )
    }

    override suspend fun delete(id: Long): NetworkResult<Unit> = safeApiCall {
        runCatching { api.deletePost(id) }
        dao.deleteById(id)
    }
}

private fun NoteDto.toEntity(): NoteEntity = NoteEntity(
    id = id ?: 0L,
    title = title,
    content = body,
    createdAt = System.currentTimeMillis(),
    updatedAt = 0L,
    type = NoteType.Text,
)
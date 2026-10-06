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

class SyncedNotesRepository(
    private val dao: NoteDao,
    private val api: NoteApi,
) {

    fun observeAll(): Flow<List<Note>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    fun search(query: String): Flow<List<Note>> =
        dao.searchByTitle(query).map { list -> list.map { it.toDomain() } }

    suspend fun getById(id: Long): Note? =
        dao.getById(id)?.toDomain()


    suspend fun refresh(): NetworkResult<Unit> = safeApiCall {
        android.util.Log.d("NotesApp", "refresh: start")
        val posts = api.listPosts()
        android.util.Log.d("NotesApp", "refresh: got ${posts.size} posts")
        val entities = posts.map { it.toEntity() }
        dao.insertAll(entities)
        android.util.Log.d("NotesApp", "refresh: inserted")
    }
    suspend fun addLocal(note: Note) {
        dao.insert(
            NoteEntity(
                title = note.title,
                content = note.content,
                createdAt = note.createdAt,
                type = note.type,
            )
        )
    }
    suspend fun add(note: Note): NetworkResult<Long> = safeApiCall {
        val created = api.createPost(NoteDto(title = note.title, body = note.content))
        // JSONPlaceholder возвращает id, но локально генерируем свой autoGenerate
        dao.insert(
            NoteEntity(
                title = created.title,
                content = created.body,
                createdAt = System.currentTimeMillis(),
                type = note.type,
            )
        )
    }

    suspend fun delete(id: Long): NetworkResult<Unit> = safeApiCall {
        // JSONPlaceholder вернёт 200 даже если не существует
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
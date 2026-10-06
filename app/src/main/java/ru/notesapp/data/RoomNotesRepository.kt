package ru.notesapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.notesapp.data.local.NoteDao
import ru.notesapp.data.local.toDomain
import ru.notesapp.data.local.toEntity
import ru.notesapp.domain.Note

class RoomNotesRepository(private val dao: NoteDao) {

    fun observeAll(): Flow<List<Note>> =
        dao.observeAll().map { list -> list.map { it.toDomain() } }

    fun search(query: String): Flow<List<Note>> =
        dao.searchByTitle(query).map { list -> list.map { it.toDomain() } }

    suspend fun getById(id: Long): Note? =
        dao.getById(id)?.toDomain()

    suspend fun add(note: Note): Long =
        dao.insert(note.toEntity())

    suspend fun update(note: Note) =
        dao.update(note.toEntity())

    suspend fun delete(id: Long) =
        dao.deleteById(id)
}
package ru.notesapp.data

import kotlinx.coroutines.flow.Flow
import ru.notesapp.data.remote.NetworkResult
import ru.notesapp.domain.Note

interface NotesRepository {
    fun observeAll(): Flow<List<Note>>
    fun search(query: String): Flow<List<Note>>
    suspend fun getById(id: Long): Note?
    suspend fun refresh(): NetworkResult<Unit>
    suspend fun add(note: Note): NetworkResult<Long>
    suspend fun delete(id: Long): NetworkResult<Unit>
}
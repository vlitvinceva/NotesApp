package ru.notesapp.data

import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType

class InMemoryNotesRepository {
    private val notes = mutableListOf(
        Note(1, "Первая заметка", "Текст первой заметки", System.currentTimeMillis(), NoteType.Text),
        Note(2, "Картинка", "Описание картинки", System.currentTimeMillis(), NoteType.Image),
    )

    fun getAll(): List<Note> = notes.toList()
    fun getById(id: Long): Note? = notes.find { it.id == id }

    fun add(note: Note): Long {
        val newId = (notes.maxOfOrNull { it.id } ?: 0L) + 1
        notes.add(note.copy(id = newId))
        return newId
    }

    fun delete(id: Long): Boolean = notes.removeIf { it.id == id }

    fun search(query: String): List<Note> =
        notes.filter { it.title.contains(query, ignoreCase = true) }
}
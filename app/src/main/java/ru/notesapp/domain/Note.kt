package ru.notesapp.domain

sealed class NoteType {
    data object Text : NoteType()
    data object Image : NoteType()
    data object Audio : NoteType()
}

data class Note(
    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
    val type: NoteType,
    val imageUrl: String? = null,
)
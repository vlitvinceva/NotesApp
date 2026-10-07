package ru.notesapp.data.local

import ru.notesapp.domain.Note

fun NoteEntity.toDomain(): Note = Note(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    type = type,
    imageUrl = imageUrl,
)

fun Note.toEntity(): NoteEntity = NoteEntity(
    id = id,
    title = title,
    content = content,
    createdAt = createdAt,
    updatedAt = System.currentTimeMillis(),
    type = type,
    imageUrl = imageUrl,
)
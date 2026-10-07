package ru.notesapp.domain

import ru.notesapp.util.MiniSerializable


@MiniSerializable
data class SimpleNote(

    val id: Long,
    val title: String,
    val content: String,
    val createdAt: Long,
)

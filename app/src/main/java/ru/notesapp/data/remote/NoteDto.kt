package ru.notesapp.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NoteDto(
    val id: Long? = null,
    val title: String,
    val body: String,
    val userId: Long? = null,
)
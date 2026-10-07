package ru.notesapp.domain

import ru.notesapp.util.MiniSerializable

@MiniSerializable(name = "user")
data class User(
    @MiniSerializable(name = "user_id") val id: Long,
    @MiniSerializable(name = "user_name") val name: String,
    val email: String?,   // ← nullable
    val age: Int = 0,
)
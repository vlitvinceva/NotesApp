package ru.notesapp.domain

fun Note.summary(maxChars: Int = 50): String = buildString {
    append("[")
    append(
        when (type) {
            is NoteType.Text -> "T"
            is NoteType.Image -> "I"
            is NoteType.Audio -> "A"
        }
    )
    append("] ")
    append(if (content.length <= maxChars) content else content.take(maxChars) + "…")
}
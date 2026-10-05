package ru.notesapp.data.local

import androidx.room.TypeConverter
import ru.notesapp.domain.NoteType

class NoteTypeConverter {
    @TypeConverter
    fun fromType(t: NoteType): String = when (t) {
        is NoteType.Text -> "TEXT"
        is NoteType.Image -> "IMAGE"
        is NoteType.Audio -> "AUDIO"
    }

    @TypeConverter
    fun toType(s: String): NoteType = when (s) {
        "TEXT" -> NoteType.Text
        "IMAGE" -> NoteType.Image
        "AUDIO" -> NoteType.Audio
        else -> error("Unknown NoteType: $s")
    }
}
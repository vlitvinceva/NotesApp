package ru.notesapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import ru.notesapp.domain.summary

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Text("NotesApp — практика 1")
            }
        }

        val notes = listOf(
            Note(1, "Первая", "Очень длинный текст первой заметки, который нужно обрезать", System.currentTimeMillis(), NoteType.Text),
            Note(2, "Картинка", "Описание картинки", System.currentTimeMillis(), NoteType.Image),
            Note(3, "Аудио", "Запись лекции", System.currentTimeMillis(), NoteType.Audio),
        )
        notes.forEach { Log.d("NotesApp", it.summary()) }
    }
}
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
import androidx.compose.runtime.remember
import ru.notesapp.ui.NoteList

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                val notes = remember {
                    listOf(
                        Note(1, "Первая", "Текст первой заметки", 0L, NoteType.Text),
                        Note(2, "Вторая", "Описание картинки", 0L, NoteType.Image),
                        Note(3, "Третья", "Запись лекции", 0L, NoteType.Audio),
                        Note(4, "Четвёртая", "Длинный текст", 0L, NoteType.Text),
                        Note(5, "Пятая", "Ещё текст", 0L, NoteType.Text),
                    )
                }
                NoteList(notes = notes, onClick = { Log.d("NotesApp", "Clicked: ${it.id}") })
            }
        }
    }
}
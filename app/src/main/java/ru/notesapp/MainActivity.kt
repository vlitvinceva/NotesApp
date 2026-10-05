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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import ru.notesapp.ui.theme.ThemeVariant
import ru.notesapp.ui.theme.NotesAppTheme
import ru.notesapp.ui.RootScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var theme by remember { mutableStateOf(ThemeVariant.PASTEL) }
            NotesAppTheme(themeVariant = theme) {
                RootScreen(onThemeChange = { theme = it }, currentTheme = theme)
            }
        }
    }
}
package ru.notesapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import ru.notesapp.data.RoomNotesRepository
import ru.notesapp.data.local.AppDatabase
import ru.notesapp.data.local.MIGRATION_1_2
import ru.notesapp.ui.RootScreen
import ru.notesapp.ui.theme.NotesAppTheme
import ru.notesapp.ui.theme.ThemeVariant
import ru.notesapp.viewmodel.NotesViewModel
import ru.notesapp.viewmodel.NotesViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. Создаём Room-базу
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "notes.db",
        )
            .addMigrations(MIGRATION_1_2)
            .build()

        // 2. Создаём репозиторий
        val repository = RoomNotesRepository(db.noteDao())

        // 3. Создаём фабрику
        val factory = NotesViewModelFactory(repository)

        setContent {
            var theme by remember { mutableStateOf(ThemeVariant.PASTEL) }
            NotesAppTheme(themeVariant = theme) {
                val vm: NotesViewModel = viewModel(factory = factory)
                RootScreen(
                    viewModel = vm,
                    onThemeChange = { theme = it },
                    currentTheme = theme,
                )
            }
        }
    }
}
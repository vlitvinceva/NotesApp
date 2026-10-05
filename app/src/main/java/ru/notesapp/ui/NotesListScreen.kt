package ru.notesapp.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.launch
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import ru.notesapp.ui.theme.NotesAppTheme
import ru.notesapp.ui.theme.ThemeVariant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    notes: List<Note>,
    onNoteClick: (Note) -> Unit,
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Заметки") },
                actions = {
                    IconButton(onClick = { /* поиск */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск")
                    }
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                scope.launch {
                    val result = snackbarHostState.showSnackbar(
                        message = "Создание новой заметки",
                        actionLabel = "Отмена",
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        Log.d("NotesApp", "Action: ОТМЕНА")
                    }
                }
            }) { Icon(Icons.Default.Add, contentDescription = "Добавить") }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { inner ->
        Box(modifier = Modifier.padding(inner).fillMaxSize()) {
            NoteList(notes = notes, onClick = onNoteClick)
        }
    }
}

@Preview
@Composable
private fun NotesListScreenLightPastel() {
    NotesAppTheme(darkTheme = false, themeVariant = ThemeVariant.PASTEL) {
        NotesListScreen(notes = demoNotes(), onNoteClick = {})
    }
}

@Preview
@Composable
private fun NotesListScreenDarkForest() {
    NotesAppTheme(darkTheme = true, themeVariant = ThemeVariant.FOREST) {
        NotesListScreen(notes = demoNotes(), onNoteClick = {})
    }
}

private fun demoNotes() = listOf(
    Note(1, "Первая", "Текст", 0L, NoteType.Text),
    Note(2, "Вторая", "Текст", 0L, NoteType.Image),
)
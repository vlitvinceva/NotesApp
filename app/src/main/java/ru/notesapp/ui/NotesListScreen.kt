package ru.notesapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ru.notesapp.domain.Note
import ru.notesapp.viewmodel.NotesUiState
import ru.notesapp.viewmodel.NotesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    onNoteClick: (Note) -> Unit,
    viewModel: NotesViewModel,

) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

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
            FloatingActionButton(onClick = { showDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Добавить")
            }

        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
    ) { inner ->
        Box(modifier = Modifier.padding(inner).fillMaxSize()) {
            when (val s = uiState) {
                is NotesUiState.Loading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
                is NotesUiState.Empty -> EmptyState("Пока нет заметок", "Нажмите +, чтобы создать")
                is NotesUiState.Success -> NoteList(notes = s.notes, onClick = onNoteClick)
                is NotesUiState.Error -> EmptyState("Ошибка", s.message)
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Новая заметка") },
            text = {
                Column {
                    OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Заголовок") })
                    OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Содержание") })
                }
            },
            confirmButton = {
                TextButton(
                    enabled = title.isNotBlank(),
                    onClick = {
                        viewModel.addNote(title, content)
                        title = ""; content = ""
                        showDialog = false
                    },
                ) { Text("Сохранить") }
            },
            dismissButton = { TextButton(onClick = { showDialog = false }) { Text("Отмена") } },
        )
    }
}


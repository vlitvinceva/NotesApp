package ru.notesapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import ru.notesapp.viewmodel.NotesUiState
import ru.notesapp.viewmodel.NotesViewModel
import androidx.compose.material.icons.filled.PhotoLibrary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    onNoteClick: (Note) -> Unit,
    onOpenGallery: () -> Unit,
    viewModel: NotesViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isRefreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val snackbarMessage by viewModel.snackbar.collectAsStateWithLifecycle()

    var showDialog by remember { mutableStateOf(false) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var imageUrl by remember { mutableStateOf("") }
    var tags by remember { mutableStateOf<List<String>>(emptyList()) }   // ← для тегов

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.consumeSnackbar()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Заметки") },
                actions = {
                    IconButton(onClick = onOpenGallery) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = "Галерея")
                    }
                    if (isRefreshing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                        )
                    } else {
                        IconButton(onClick = { viewModel.refresh() }) {
                            Icon(Icons.Default.Refresh, contentDescription = "Обновить")
                        }
                    }
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
        Column(modifier = Modifier.padding(inner).fillMaxSize()) {
            // ✅ Баннер под TopAppBar (интероп через AndroidView)
            BannerAdView(
                adUnitId = "demo-banner",
                modifier = Modifier.fillMaxWidth(),
            )

            Box(modifier = Modifier.fillMaxSize()) {
                when (val s = uiState) {
                    is NotesUiState.Loading -> Box(
                        Modifier.fillMaxSize(),
                        Alignment.Center,
                    ) { CircularProgressIndicator() }

                    is NotesUiState.Empty -> EmptyState(
                        title = "Пока нет заметок",
                        subtitle = "Нажмите +, чтобы создать первую",
                    )

                    is NotesUiState.Success -> NoteList(
                        notes = s.notes,
                        onClick = onNoteClick,
                    )

                    is NotesUiState.Error -> EmptyState(
                        title = "Ошибка",
                        subtitle = s.message,
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Новая заметка") },
            text = {
                Column {
                    OutlinedTextField(
                        value = title,
                        onValueChange = { title = it },
                        label = { Text("Заголовок") },
                    )
                    OutlinedTextField(
                        value = content,
                        onValueChange = { content = it },
                        label = { Text("Содержание") },
                    )
                    OutlinedTextField(
                        value = imageUrl,
                        onValueChange = { imageUrl = it },
                        label = { Text("URL изображения (опционально)") },
                    )

                    TagInputInline(
                        tags = tags,
                        onTagAdded = { tags = tags + it },
                        onTagRemoved = { tags = tags - it },
                    )
                }
            },
            confirmButton = {
                TextButton(
                    enabled = title.isNotBlank(),
                    onClick = {
                        val type = if (imageUrl.isNotBlank()) NoteType.Image else NoteType.Text
                        viewModel.addNote(
                            title = title,
                            content = content,
                            type = type,
                            imageUrl = imageUrl.takeIf { it.isNotBlank() },
                        )
                        title = ""
                        content = ""
                        imageUrl = ""
                        tags = emptyList()
                        showDialog = false
                    },
                ) { Text("Сохранить") }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) { Text("Отмена") }
            },
        )
    }
}

@Composable
private fun TagInputInline(
    tags: List<String>,
    onTagAdded: (String) -> Unit,
    onTagRemoved: (String) -> Unit,
) {
    var input by remember { mutableStateOf("") }
    Column {
        androidx.compose.foundation.layout.Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                label = { Text("Тег") },
                modifier = Modifier.weight(1f),
            )
            androidx.compose.foundation.layout.Spacer(Modifier.size(8.dp))
            TextButton(onClick = {
                if (input.isNotBlank()) {
                    onTagAdded(input.trim())
                    input = ""
                }
            }) { Text("+") }
        }
        androidx.compose.foundation.layout.FlowRow(
            horizontalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(4.dp),
        ) {
            tags.forEach { tag ->
                androidx.compose.material3.AssistChip(
                    onClick = { onTagRemoved(tag) },
                    label = { Text(tag) },
                )
            }
        }
    }
}
package ru.notesapp.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.notesapp.domain.NoteType
import ru.notesapp.viewmodel.NoteDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.compose.ui.platform.LocalLocale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Long,
    onBack: () -> Unit,
    viewModel: NoteDetailViewModel = hiltViewModel(),
) {
    // Загружаем заметку при первом появлении экрана
    LaunchedEffect(noteId) {
        viewModel.load(noteId)
    }

    val note by viewModel.note.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Заметка #$noteId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Назад")
                    }
                },
            )
        },
    ) { inner ->
        when {
            loading -> Box(
                Modifier.padding(inner).fillMaxSize(),
                contentAlignment = androidx.compose.ui.Alignment.Center,
            ) { CircularProgressIndicator() }

            note == null -> EmptyState(
                title = "Заметка не найдена",
                subtitle = "Вернитесь назад",
                modifier = Modifier.padding(inner),
            )

            else -> {
                val n = note!!
                Column(
                    modifier = Modifier
                        .padding(inner)
                        .padding(16.dp)
                        .fillMaxSize(),
                ) {
                    Text(n.title, style = MaterialTheme.typography.titleLarge)

                    val typeLabel = when (n.type) {
                        is NoteType.Text -> "ТЕКСТ"
                        is NoteType.Image -> "КАРТИНКА"
                        is NoteType.Audio -> "АУДИО"
                    }
                    Text(typeLabel, style = MaterialTheme.typography.labelMedium)

                    val fmt = SimpleDateFormat("dd.MM.yyyy HH:mm", LocalLocale.current.platformLocale)
                    Text(fmt.format(Date(n.createdAt)), style = MaterialTheme.typography.labelMedium)

                    Spacer(Modifier.height(16.dp))
                    Text(n.content, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
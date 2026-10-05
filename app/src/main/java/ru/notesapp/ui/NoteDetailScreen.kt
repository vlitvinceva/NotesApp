package ru.notesapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteDetailScreen(
    noteId: Long,
    onBack: () -> Unit,
) {
    val note = demoNotes().find { it.id == noteId }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Заметка #$noteId") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Назад",
                        )
                    }
                },
            )
        },
    ) { inner ->
        if (note == null) {
            EmptyState(
                title = "Заметка не найдена",
                subtitle = "Вернитесь назад",
                modifier = Modifier.padding(inner),
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(inner)
                    .padding(16.dp)
                    .fillMaxSize(),
            ) {
                Text(
                    text = note.title,
                    style = MaterialTheme.typography.titleLarge,
                )

                // ✅ ИСПРАВЛЕНО: вместо note.type.name используем when
                val typeLabel = when (note.type) {
                    is NoteType.Text -> "ТЕКСТ"
                    is NoteType.Image -> "КАРТИНКА"
                    is NoteType.Audio -> "АУДИО"
                }
                Text(
                    text = typeLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary,
                )

                val fmt = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault())
                Text(
                    text = fmt.format(Date(note.createdAt)),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.outline,
                )

                Spacer(Modifier.height(16.dp))

                Text(
                    text = note.content,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

private fun demoNotes() = listOf(
    Note(1, "Первая", "Полный текст первой заметки", System.currentTimeMillis(), NoteType.Text),
    Note(2, "Вторая", "Описание картинки", System.currentTimeMillis(), NoteType.Image),
    Note(3, "Третья", "Запись лекции", System.currentTimeMillis(), NoteType.Audio),
    Note(4, "Четвёртая", "Длинный текст", System.currentTimeMillis(), NoteType.Text),
    Note(5, "Пятая", "Ещё текст", System.currentTimeMillis(), NoteType.Text),
)
package ru.notesapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.notesapp.domain.Note

@Composable
fun NoteList(
    notes: List<Note>,
    onClick: (Note) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (notes.isEmpty()) {
        EmptyState("Пока нет заметок", "Нажмите +, чтобы создать первую", modifier)
        return
    }
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(notes, key = { it.id }) { note ->
            NoteCard(note = note, onClick = { onClick(note) })
        }
    }
}
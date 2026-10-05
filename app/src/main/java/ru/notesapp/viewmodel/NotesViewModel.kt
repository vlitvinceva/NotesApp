package ru.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import ru.notesapp.data.InMemoryNotesRepository
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType

class NotesViewModel(
    private val repository: InMemoryNotesRepository = InMemoryNotesRepository(),
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private var allNotes: List<Note> = emptyList()

    init { loadNotes() }

    fun loadNotes() {
        _uiState.value = NotesUiState.Loading
        viewModelScope.launch {
            delay(500)
            allNotes = withContext(Dispatchers.IO) { repository.getAll() }
            updateState(allNotes)
        }
    }

    fun addNote(title: String, content: String, type: NoteType = NoteType.Text) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { repository.add(Note(0, title, content, System.currentTimeMillis(), type)) }
            allNotes = repository.getAll()
            updateState(allNotes)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) { repository.delete(id) }
            allNotes = repository.getAll()
            updateState(allNotes)
        }
    }

    fun searchNotes(query: String) {
        viewModelScope.launch {
            val list = if (query.isBlank()) allNotes else withContext(Dispatchers.IO) { repository.search(query) }
            updateState(list)
        }
    }

    private fun updateState(list: List<Note>) {
        _uiState.value = if (list.isEmpty()) NotesUiState.Empty else NotesUiState.Success(list)
    }
}
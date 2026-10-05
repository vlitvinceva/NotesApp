package ru.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.notesapp.data.RoomNotesRepository
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val repository: RoomNotesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val query = MutableStateFlow("")

    init {
        viewModelScope.launch {
            query.flatMapLatest { q ->
                if (q.isBlank()) repository.observeAll()
                else repository.search(q)
            }.collect { notes ->
                _uiState.value = if (notes.isEmpty()) {
                    NotesUiState.Empty
                } else {
                    NotesUiState.Success(notes)
                }
            }
        }
    }

    fun addNote(title: String, content: String, type: NoteType = NoteType.Text) {
        viewModelScope.launch {
            repository.add(Note(0, title, content, System.currentTimeMillis(), type))
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch { repository.delete(id) }
    }

    fun searchNotes(q: String) {
        query.value = q
    }
}
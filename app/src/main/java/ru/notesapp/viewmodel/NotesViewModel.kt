package ru.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import ru.notesapp.data.SyncedNotesRepository
import ru.notesapp.data.remote.NetworkResult
import ru.notesapp.domain.Note
import ru.notesapp.domain.NoteType

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModel(
    private val repository: SyncedNotesRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _snackbar = MutableStateFlow<String?>(null)
    val snackbar: StateFlow<String?> = _snackbar.asStateFlow()

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

    fun refresh() {
        if (_isRefreshing.value) return
        viewModelScope.launch {
            _isRefreshing.value = true
            when (val r = repository.refresh()) {
                is NetworkResult.Success -> _snackbar.value = "Обновлено"
                is NetworkResult.Error -> _snackbar.value = "Ошибка сервера: ${r.code}"
                is NetworkResult.NetworkError -> _snackbar.value = "Нет соединения с интернетом"
            }
            _isRefreshing.value = false
        }
    }

    fun addNote(title: String, content: String, type: NoteType = NoteType.Text) {
        if (title.isBlank()) return
        viewModelScope.launch {
            try {
                repository.addLocal(Note(0, title, content, System.currentTimeMillis(), type))
                _snackbar.value = "Заметка создана"
            } catch (e: Exception) {
                android.util.Log.e("NotesApp", "addNote failed", e)
                _snackbar.value = "Ошибка: ${e.message}"
            }
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.delete(id)
        }
    }

    fun searchNotes(q: String) {
        query.value = q
    }

    fun consumeSnackbar() {
        _snackbar.value = null
    }
}
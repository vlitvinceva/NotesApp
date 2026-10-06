package ru.notesapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.notesapp.data.NotesRepository
import ru.notesapp.domain.Note
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NotesRepository,
) : ViewModel() {

    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    fun load(id: Long) {
        viewModelScope.launch {
            _loading.value = true
            _note.value = repository.getById(id)
            _loading.value = false
        }
    }
}
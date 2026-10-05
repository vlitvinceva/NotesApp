package ru.notesapp.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*

fun <T> searchFlow(
    queries: Flow<String>,
    minLength: Int = 2,
    debounceMs: Long = 300,
    search: suspend (String) -> List<T>,
): Flow<List<T>> = queries
    .debounce(debounceMs)
    .distinctUntilChanged()
    .filter { it.length >= minLength || it.isEmpty() }
    .map { q -> if (q.isEmpty()) emptyList() else search(q) }
    .catch { emit(emptyList()) }
    .flowOn(Dispatchers.IO)
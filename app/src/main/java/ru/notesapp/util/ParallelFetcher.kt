package ru.notesapp.util

import kotlinx.coroutines.async
import kotlinx.coroutines.supervisorScope

suspend fun fetchAll(
    urls: List<String>,
    fetch: suspend (String) -> String,
): List<Result<String>> = supervisorScope {
    urls.map { url ->
        async {
            runCatching { fetch(url) }
        }
    }.map { it.await() }
}
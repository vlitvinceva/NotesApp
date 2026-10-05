package ru.notesapp.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class CoroutinesTest {

    // ===== countdown =====

    @Test
    fun `countdown emits from 3 to 0`() = runTest {
        val result = countdown(3, tickMs = 100).toList()
        assertEquals(listOf(3, 2, 1, 0), result)
    }

    @Test
    fun `countdown completes after zero`() = runTest {
        val list = countdown(0, tickMs = 100).toList()
        assertEquals(listOf(0), list)
    }

    @Test
    fun `countdown cancellable`() = runTest {
        val result = withTimeoutOrNull(50) {
            countdown(100, tickMs = 1000).toList()
        }
        assertEquals(null, result)
    }

    // ===== fetchAll =====

    @Test
    fun `fetchAll parallel returns all results`() = runTest {
        val urls = listOf("a", "b", "c")
        val results = fetchAll(urls) { "Response for $it" }
        assertEquals(3, results.size)
        assertTrue(results.all { it.isSuccess })
        assertEquals("Response for a", results[0].getOrNull())
    }

    @Test
    fun `fetchAll one fails others succeed`() = runTest {
        val urls = listOf("ok1", "fail", "ok2")
        val results = fetchAll(urls) { url ->
            if (url == "fail") error("boom") else "Response for $url"
        }
        assertEquals(3, results.size)
        assertEquals(2, results.count { it.isSuccess })
        assertEquals(1, results.count { it.isFailure })
        assertTrue(results[1].isFailure)
    }

    // ===== searchFlow =====

    @Test
    fun `searchFlow debounce skips intermediate`() = runTest {
        val queries = flow {
            emit("пр")
            emit("при")
            emit("привет")
        }
        val calls = mutableListOf<String>()
        searchFlow(queries, debounceMs = 100) { q ->
            calls.add(q)
            listOf(q)
        }.toList()

        assertEquals(listOf("привет"), calls)
    }

    @Test
    fun `searchFlow empty query returns empty list`() = runTest {
        val calls = mutableListOf<String>()
        val results = searchFlow(flowOf(""), debounceMs = 0) { q ->
            calls.add(q)
            listOf(q)
        }.toList()

        assertEquals(listOf(emptyList<String>()), results)
        assertTrue(calls.isEmpty())
    }

    @Test
    fun `searchFlow min length filter`() = runTest {
        val calls = mutableListOf<String>()
        searchFlow(flowOf("a"), debounceMs = 0) { q ->
            calls.add(q)
            listOf(q)
        }.toList()

        assertTrue(calls.isEmpty())
    }

    @Test
    fun `searchFlow catch emits empty on error`() = runTest {
        // ✅ ЯВНО указываем T = String
        val flow: Flow<List<String>> = searchFlow(flowOf("привет"), debounceMs = 0) { _ ->
            error("network fail")
        }
        val result = flow.toList()
        assertEquals(listOf(emptyList<String>()), result)
    }
}
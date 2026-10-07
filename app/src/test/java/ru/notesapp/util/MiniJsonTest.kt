package ru.notesapp.util

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import ru.notesapp.domain.SimpleNote
import ru.notesapp.domain.User

class MiniJsonTest {


    @Test
    fun `toJson string field wrapped in quotes`() {
        val u = User(1L, "Alice", "a@b.com", 30)
        val json = u.toJson()
        assertTrue(json.contains("\"user_name\": \"Alice\""))
    }

    @Test
    fun `toJson long field no quotes`() {
        val u = User(1L, "Alice", "a@b.com", 30)
        assertTrue(u.toJson().contains("\"user_id\": 1"))
    }

    @Test
    fun `toJson int field no quotes`() {
        val u = User(1L, "Alice", "a@b.com", 30)
        assertTrue(u.toJson().contains("\"age\": 30"))
    }

    @Test
    fun `toJson null field emits null without quotes`() {
        val u = User(1L, "Alice", null, 0)
        assertTrue(u.toJson().contains("\"email\": null"))
        assertFalse(u.toJson().contains("\"email\": \"null\""))
    }


    @Test
    fun `toJson single field object starts and ends with braces`() {
        val n = SimpleNote(1L, "Заголовок", "Содержимое", 123456L)
        val json = n.toJson()
        assertTrue(json.startsWith("{"))
        assertTrue(json.endsWith("}"))
    }

    @Test
    fun `toJson field separator is comma`() {
        val n = SimpleNote(1L, "a", "b", 0L)
        assertTrue(n.toJson().contains(", "))
    }

    @Test
    fun `toJson key value separator is colon`() {
        val n = SimpleNote(1L, "a", "b", 0L)
        assertTrue(n.toJson().contains("\": "))
    }


    @Test
    fun `toJson uses annotated name when present`() {
        val u = User(42L, "Bob", null, 0)
        val json = u.toJson()
        assertTrue(json.contains("\"user_id\": 42"))
        assertTrue(json.contains("\"user_name\": \"Bob\""))
    }

    @Test
    fun `toJson uses property name when annotation absent`() {
        val u = User(1L, "Alice", "x@y.com", 5)
        val json = u.toJson()
        assertTrue(json.contains("\"email\": "))
        assertTrue(json.contains("\"age\": 5"))
    }

    @Test
    fun `toJson class annotation not used in output`() {
        val u = User(1L, "Alice", null, 0)
        val json = u.toJson()
        assertFalse(json.contains("\"user\": {"))
    }


    @Test
    fun `toJson string with quotes escaped`() {
        val u = User(1L, "He said \"hi\"", null, 0)
        assertTrue(u.toJson().contains("\\\"hi\\\""))
    }

    @Test
    fun `toJson string with backslash escaped`() {
        val u = User(1L, "path\\to\\file", null, 0)
        assertTrue(u.toJson().contains("\\\\"))
    }

    @Test
    fun `toJson string with newline escaped`() {
        val u = User(1L, "line1\nline2", null, 0)
        assertTrue(u.toJson().contains("\\n"))
    }

    @Test
    fun `toJson string with tab escaped`() {
        val u = User(1L, "a\tb", null, 0)
        assertTrue(u.toJson().contains("\\t"))
    }


    @Test(expected = IllegalArgumentException::class)
    fun `toJson class without annotation throws`() {
        data class Bad(val x: Int)
        Bad(1).toJson()
    }

    @Test
    fun `toJson class without annotation error message includes class name`() {
        data class Bad(val x: Int)
        try {
            Bad(1).toJson()
        } catch (e: IllegalArgumentException) {
            assertTrue(e.message!!.contains("Bad"))
        }
    }


    @Test
    fun `fromJson user with email round trip`() {
        val original = User(1L, "Alice", "a@b.com", 30)
        val json = original.toJson()
        val parsed = fromJson(json, User::class)
        assertEquals(original, parsed)
    }

    @Test
    fun `fromJson user with null email round trip`() {
        val original = User(7L, "Bob", null, 25)
        val json = original.toJson()
        val parsed = fromJson(json, User::class)
        assertEquals(original, parsed)
    }

    @Test
    fun `fromJson simple note round trip`() {
        val original = SimpleNote(10L, "Title", "Content text", 123456789L)
        val json = original.toJson()
        val parsed = fromJson(json, SimpleNote::class)
        assertEquals(original, parsed)
    }

    @Test
    fun `fromJson missing optional field uses default`() {
        val json = """{"user_id": 1, "user_name": "Alice", "email": null}"""
        val parsed = fromJson(json, User::class)
        assertEquals(0, parsed.age)
    }

    @Test
    fun `round trip unicode strings preserved`() {
        val u = User(1L, "Пример с юникодом: 你好 🐱", null, 0)
        val parsed = fromJson(u.toJson(), User::class)
        assertEquals(u.name, parsed.name)
    }
}
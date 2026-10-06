package ru.notesapp.data.remote

import retrofit2.HttpException
import java.io.IOException

sealed interface NetworkResult<out T> {
    data class Success<T>(val data: T) : NetworkResult<T>
    data class Error(val code: Int, val message: String) : NetworkResult<Nothing>
    data class NetworkError(val exception: IOException) : NetworkResult<Nothing>
}

suspend fun <T> safeApiCall(block: suspend () -> T): NetworkResult<T> = try {
    NetworkResult.Success(block())
} catch (e: HttpException) {
    NetworkResult.Error(e.code(), e.message ?: "HTTP ${e.code()}")
} catch (e: IOException) {
    NetworkResult.NetworkError(e)
} catch (e: Exception) {
    // Ловим всё остальное: Moshi, Retrofit, IllegalStateException
    android.util.Log.e("NotesApp", "safeApiCall unexpected", e)
    NetworkResult.Error(-1, e.message ?: e::class.java.simpleName)
}
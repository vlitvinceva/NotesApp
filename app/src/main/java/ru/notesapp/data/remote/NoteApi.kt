package ru.notesapp.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface NoteApi {

    @GET("posts")
    suspend fun listPosts(): List<NoteDto>

    @GET("posts/{id}")
    suspend fun getPost(@Path("id") id: Long): NoteDto

    @POST("posts")
    suspend fun createPost(@Body post: NoteDto): NoteDto

    @PUT("posts/{id}")
    suspend fun updatePost(@Path("id") id: Long, @Body post: NoteDto): NoteDto

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Long)
}
package com.persol.mytodoapp.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface PostApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @PUT("post/{id}")
    suspend fun updatePost(@Path("id") id: Int, @Body post: Post): Response<Post>

    //delete post
    @DELETE("post/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<Post>

    @POST("post")
    suspend fun createPost(post: Post): Response<Post>



}
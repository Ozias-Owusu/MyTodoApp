package com.persol.mytodoapp.network

import retrofit2.http.GET

interface PostApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

}
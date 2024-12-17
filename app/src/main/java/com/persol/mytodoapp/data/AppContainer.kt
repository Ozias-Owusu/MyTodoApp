package com.persol.mytodoapp.data

import com.persol.mytodoapp.network.PostApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

interface AppContainer {
    val postRepository: PostRepository

}

class DefaultAppContainer: AppContainer {
    private val baseUrl =
        "https://jsonplaceholder.typicode.com"

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()
    private val retrofitService: PostApiService by lazy {
        retrofit.create(PostApiService::class.java)
    }
    override val postRepository: PostRepository by lazy {
        NetworkPostRepository(retrofitService)
    }

}
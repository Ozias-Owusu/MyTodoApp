package com.persol.mytodoapp.data

import com.persol.mytodoapp.network.Post
import com.persol.mytodoapp.network.PostApiService

interface PostRepository {
    suspend fun getPosts(): List<Post>
}

class NetworkPostRepository (
    private val postApiService: PostApiService
) : PostRepository {
    override suspend fun getPosts(): List<Post> {
        return postApiService.getPosts()
    }
}


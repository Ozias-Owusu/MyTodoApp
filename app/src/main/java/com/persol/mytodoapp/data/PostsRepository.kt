package com.persol.mytodoapp.data

import com.persol.mytodoapp.network.Post
import com.persol.mytodoapp.network.PostApiService
import retrofit2.Response

interface PostRepository {
    suspend fun getPosts(): List<Post>
    suspend fun updatePost(id: Int, post: Post): Response<Post>
    suspend fun deletePost(id: Int, post: Post): Response<Post>
    suspend fun createPost(post: Post): Response<Post>
}

class NetworkPostRepository (
    private val postApiService: PostApiService
) : PostRepository {
    override suspend fun getPosts(): List<Post> {
        return postApiService.getPosts()
    }
    override suspend fun updatePost(id: Int, post: Post): Response<Post> = postApiService.updatePost(id, post)
    override suspend fun deletePost(id: Int, post: Post): Response<Post> = postApiService.deletePost(id)
    override suspend fun createPost(post: Post): Response<Post> = postApiService.createPost(post)
    }



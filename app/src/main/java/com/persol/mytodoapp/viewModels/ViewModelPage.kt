package com.persol.mytodoapp.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.persol.mytodoapp.appmodule.PostItemApplication
import com.persol.mytodoapp.database.TodoDao
import com.persol.mytodoapp.screens.TodoItem
import com.persol.mytodoapp.data.PostRepository
import com.persol.mytodoapp.network.Post
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

sealed interface PostUiState {
    data class Success(val posts: List<Post>) : PostUiState
    data object Error : PostUiState
    data object Loading : PostUiState
}

class PostViewModel(private val postRepository: PostRepository) : ViewModel() {
    var postUiState: PostUiState by mutableStateOf(PostUiState.Loading)
        private set
    init {
        getPostItems()
    }

    fun getPostItems() {
        viewModelScope.launch {
            postUiState = try {
                PostUiState.Success(postRepository.getPosts())
            } catch (e: IOException) {
                Log.e("PostViewModel", "Error loading posts", e)
                e.printStackTrace()
                PostUiState.Error
            }
        }
    }
//    suspend fun updatePost(post: Post): Response<Post> {
//        return try {
//            postRepository.updatePost(post)
//            Response.success(post)
//        } catch (e: IOException) {
//            Log.e("PostViewModel", "Error updating post", e)
//            e.printStackTrace()
//            Response.error<>()
//
//        }
//    }
    suspend fun updatePost(post: Post): String {
    var response = ""
    try {
        val result = postRepository.updatePost(post.id, post)
        if (result.isSuccessful) {
            response = "Post updated successfully"

        } else {
            response = "Failed to update post"
            Log.e("PostViewModel", "Error updating post: ${result.errorBody()?.string()}")
        }
    } catch (e: Exception) {
        Log.e("PostViewModel", "Error updating post", e)
    }
    return response
}
    suspend fun deletePost(post: Post): String {
        var response = ""
        try {
            val result = postRepository.deletePost(post.id, post)
            if (result.isSuccessful) {
                response = "Post deleted successfully"
    }else{
                response = "Failed to delete post"
            }
        } catch (e: Exception) {
            Log.e("PostViewModel", "Error deleting post", e)
        }
        return response
            }

    suspend fun createPost(post: Post): String {
        var response = ""
try {
    val result = postRepository.createPost(post)
    if (result.isSuccessful) {
        response = "Post created successfully"
    } else {
        response = "Failed to create post"}

}catch (e: Exception){
    Log.e("PostViewModel", "Error creating post", e)
}
        return response
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostItemApplication)
                val postRepository = application.container.postRepository
                PostViewModel(postRepository = postRepository)
            }
        }
    }
}

class TodoViewModel(private val todoDao: TodoDao) : ViewModel() {
    val todoList = mutableStateListOf<TodoItem>()
    val  completedTodoList = mutableStateListOf<TodoItem>()

    init {
        viewModelScope.launch {
            todoDao.getAllTodos().collect { todosFromDb ->
                todoList.clear()
                todoList.addAll(todosFromDb.filter { !it.isCompleted })
                completedTodoList.clear()
                completedTodoList.addAll(todosFromDb.filter { it.isCompleted })
            }
        }
    }

//    fun addTodo(todo: TodoItem) {
//        todoList.add(todo)
//        databaseAddTodo(todo)
//    }

    suspend fun addTodo(todo: TodoItem): Boolean {
        return try {
            todoList.add(todo)
            todoDao.insertTodo(todo)
            true
        } catch (e: Exception) {
            false
        }
    }

//    fun deleteTodo(todo: TodoItem) {
//        todoList.remove(todo)
//        databaseDeleteTodo(todo)
//    }
    suspend fun deleteTodo(todo: TodoItem): Boolean {
        return try {
            todoList.remove(todo)
            todoDao.deleteTodo(todo)
            true
        } catch (e: Exception) {
            false
        }
    }

    fun toggleTodo(todo: TodoItem) {
        todo.isCompleted = !todo.isCompleted
        if (todo.isCompleted) {
            completedTodoList.add(todo)
            todoList.remove(todo)
        } else {
            todoList.add(todo)
            completedTodoList.remove(todo)
        }
        databaseUpdateTodo(todo, todo)
    }

    fun updateTodo(oldTodo: TodoItem, newTodo: TodoItem) {
        val index = todoList.indexOf(oldTodo)
        if (index != -1) {
            todoList[index] = newTodo.copy(isCompleted = oldTodo.isCompleted)
            databaseUpdateTodo(oldTodo, newTodo) // Ensure persistence
        }
    }

    val DatabaseTodoList: StateFlow<List<TodoItem>> =
        todoDao.getAllTodos().stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    fun databaseAddTodo(todo: TodoItem) {
        viewModelScope.launch {
            todoDao.insertTodo(todo)
        }
    }

    private fun databaseUpdateTodo(existingTodo: TodoItem, newTodo: TodoItem) {
        viewModelScope.launch {
            todoDao.updateTodo(newTodo.copy(id = existingTodo.id))
        }
    }

    fun databaseDeleteTodo(todo: TodoItem) {
        viewModelScope.launch {
            todoDao.deleteTodo(todo)
        }
    }
    fun clearCompletedTodos() {
        completedTodoList.clear() // Clear the list in the UI
        viewModelScope.launch {
            todoDao.deleteAllCompletedTodos() // Remove from database
        }
    }

}
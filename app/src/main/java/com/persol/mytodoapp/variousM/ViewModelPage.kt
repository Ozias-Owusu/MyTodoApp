package com.persol.mytodoapp.variousM

import com.persol.mytodoapp.Screens.TodoItem
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
import com.persol.mytodoapp.PostItemApplication
import com.persol.mytodoapp.data.PostRepository
import com.persol.mytodoapp.network.Post
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
//import kotlinx.coroutines.flow.internal.NopCollector.emit
import kotlinx.coroutines.launch
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

class TodoViewModel(private val repository: TodoRepository) : ViewModel() {
    val todoList = mutableStateListOf<TodoItem>()
    private val  completedTodoList = mutableStateListOf<TodoItem>()
    private val _showEditDialog = MutableStateFlow<TodoItem?>(null)
    val showEditDialog: StateFlow<TodoItem?> = _showEditDialog



    fun showEditDialog(todo: TodoItem) {
        _showEditDialog.value = todo
    }

    fun hideEditDialog() {
        _showEditDialog.value = null
    }

    fun addTodo(todo: TodoItem) {
        todoList.add(todo)
    }

    fun deleteTodo(todo: TodoItem) {
        todoList.remove(todo)
    }

    fun toggleTodo(todo: TodoItem) {
        todo.isCompleted = !todo.isCompleted
        if (!todo.isCompleted) {
            completedTodoList.add(todo)
        }
    }

    fun updateTodo(oldTodo: TodoItem, newTodo: TodoItem) {
        val index = todoList.indexOf(oldTodo)
        if (index != -1) {
            todoList[index] = newTodo.copy(isCompleted = oldTodo.isCompleted)

        }
    }

}

interface TodoRepository {
    fun getTodos(): Flow<List<TodoItem>>
    fun addTodo(todo: TodoItem)
    fun updateTodo(todo: TodoItem)
    fun deleteTodo(todo: TodoItem)
}

class InMemoryTodoRepository : TodoRepository {
    private val todos = mutableStateListOf<TodoItem>()

    override fun getTodos(): Flow<List<TodoItem>> = flow {
        emit(todos.toList())
    }

    override fun addTodo(todo: TodoItem) {
        todos.add(todo)
    }

    override fun updateTodo(todo: TodoItem) {
        val index = todos.indexOfFirst { it.id == todo.id }
        if (index != -1) {
            todos[index] = todo
        }
    }

    override fun deleteTodo(todo: TodoItem) {
        todos.remove(todo)
    }
}
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

class TodoViewModel(private val todoDao: TodoDao) : ViewModel() {
    val todoList = mutableStateListOf<TodoItem>()
    private val  completedTodoList = mutableStateListOf<TodoItem>()

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

    fun addTodo(todo: TodoItem) {
        todoList.add(todo)
        databaseAddTodo(todo)
    }

    fun deleteTodo(todo: TodoItem) {
        todoList.remove(todo)
        databaseDeleteTodo(todo)
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
//        todo.isCompleted = !todo.isCompleted
//        if (!todo.isCompleted) {
//            completedTodoList.add(todo)
//        }
    }

    fun updateTodo(oldTodo: TodoItem, newTodo: TodoItem) {
        val index = todoList.indexOf(oldTodo)
        if (index != -1) {
            todoList[index] = newTodo.copy(isCompleted = oldTodo.isCompleted)
            databaseUpdateTodo(oldTodo, newTodo) // Ensure persistence
        }
//        val index = todoList.indexOf(oldTodo)
//        if (index != -1) {
//            todoList[index] = newTodo.copy(isCompleted = oldTodo.isCompleted)
//
//        }
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

    fun databaseUpdateTodo(existingTodo: TodoItem, newTodo: TodoItem) {
        viewModelScope.launch {
            todoDao.updateTodo(newTodo.copy(id = existingTodo.id))
        }
    }

    fun databaseDeleteTodo(todo: TodoItem) {
        viewModelScope.launch {
            todoDao.deleteTodo(todo)
        }
    }

}
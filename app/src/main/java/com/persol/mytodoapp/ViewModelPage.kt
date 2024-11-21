package com.persol.mytodoapp

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel

class TodoViewModel : ViewModel() {
    val todoList = mutableStateListOf<TodoItem>()
    private val  completedTodoList = mutableStateListOf<TodoItem>()

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

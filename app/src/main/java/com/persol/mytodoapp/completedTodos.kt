package com.persol.mytodoapp

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDecoration

@Composable
fun CompletedTodos(todoList: List<TodoItem>) {
    val completedTodos = todoList.filter { it.isCompleted }

    Column {
        completedTodos.forEach { todo ->
            Text(
                text = todo.text,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}
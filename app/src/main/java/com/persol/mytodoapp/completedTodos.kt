package com.persol.mytodoapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp

@Composable
fun CompletedTodos(todoList: List<TodoItem>) {
    val completedTodos = todoList.filter { it.isCompleted }

    Column(verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        completedTodos.forEach { todo ->
            Text(
                text = todo.text,
                textDecoration = TextDecoration.LineThrough
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
       Button(modifier = Modifier.align(Alignment.CenterHorizontally),
           onClick = {  }) {Text(text = "Clear Completed")}
    }
}
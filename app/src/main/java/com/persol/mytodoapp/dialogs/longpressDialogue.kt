package com.persol.mytodoapp.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.persol.mytodoapp.screens.TodoItem
import com.persol.mytodoapp.viewModels.TodoViewModel

//@Composable
//fun LongPressDialog(
//    todo: TodoItem,
//    onEdit: () -> Unit,
//    onDelete: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text(text = "Actions for ${todo.text}") },
//        text = { Text(text = "Choose an action") },
//        confirmButton = {
//            TextButton(onClick = {
//                onEdit()
//                onDismiss()
//            }) {
//                Text("Edit")
//            }
//        },
//        dismissButton = {
//            TextButton(onClick = {
//                onDelete()
//                onDismiss()
//            }) {
//                Text("Delete")
//            }
//        }
//    )
//}

@Composable
fun LongPressDialog(
    todo: TodoItem,
    onEdit: (TodoItem) -> Unit,
    viewModel: TodoViewModel,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    var showEditDialog by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Actions for ${todo.text}") },
        text = { Text(text = "Choose an action") },
        confirmButton = {
            TextButton(onClick = {
                showEditDialog = true
            }) {
                Text("Edit")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDelete()
                onDismiss()
            }) {
                Text("Delete")
            }
        }
    )

    if (showEditDialog) {
        EditTodoDialog(
            todo = todo,
            onEditTodo = { updatedTodo ->
                viewModel.updateTodo(todo, updatedTodo)
                showEditDialog = false
            },
            onDismiss = { showEditDialog = false }
        )
    }
}


@Composable
fun EditTodoDialog(
    todo: TodoItem,
    onEditTodo: (TodoItem) -> Unit,
    onDismiss: () -> Unit
) {
    var updatedTodo by remember { mutableStateOf(todo.copy()) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Todo") },
        text = {
            Column {
                TextField(
                    value = updatedTodo.text,
                    onValueChange = { updatedTodo.text = it },
                    label = { Text(text = "Todo Text") },
                    modifier = Modifier.fillMaxWidth()
                )
                // Add more fields for editing other properties as needed
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onEditTodo(updatedTodo)
                onDismiss()
            }) {
                Text("Save")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
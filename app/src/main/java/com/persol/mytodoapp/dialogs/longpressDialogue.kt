package com.persol.mytodoapp.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.persol.mytodoapp.screens.TodoItem
import com.persol.mytodoapp.screens.showDateTimePicker
import com.persol.mytodoapp.viewModels.TodoViewModel

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
    var updatedTodo by remember { mutableStateOf(todo.text) }
    var updatedDateTime by remember { mutableStateOf(todo.dateTime) }

    val context = LocalContext.current


    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Edit Todo") },
        text = {
            Column {
                TextField(
                    value = updatedTodo,
                    onValueChange = { updatedTodo = it },
                    label = { Text(text = "Todo Text") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // TextField for displaying date and time
                    TextField(
                        value = updatedDateTime,
                        onValueChange = { updatedDateTime = it },
                        label = { Text(text = "Date and Time") },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Button to trigger DateTimePicker
                    Button(onClick = {
                        // Pass current date-time to the picker
                        showDateTimePicker(
                            context = context,
                            initialDateTime = updatedDateTime,
                            onDateTimeSelected = { newDateTime ->
                                updatedDateTime = newDateTime
                            }
                        )
                    }) {
                        Text("Edit")
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = {
//                onEditTodo(todo.copy(text = updatedTodo))
//                onDismiss()
                onEditTodo(todo.copy(text = updatedTodo, dateTime = updatedDateTime))
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
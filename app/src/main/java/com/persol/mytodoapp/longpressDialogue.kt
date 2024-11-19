package com.persol.mytodoapp

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun LongPressDialog(
    todo: TodoItem,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Actions for ${todo.text}") },
        text = { Text(text = "Choose an action") },
        confirmButton = {
            TextButton(onClick = {
                onEdit()
                onDismiss()
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
}
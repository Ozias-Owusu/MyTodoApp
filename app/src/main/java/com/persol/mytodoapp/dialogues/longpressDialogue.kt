package com.persol.mytodoapp.dialogues

import com.persol.mytodoapp.Screens.TodoItem
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.persol.mytodoapp.Screens.EditTodoDialog
import com.persol.mytodoapp.variousM.TodoViewModel

@Composable
fun LongPressDialog(
    todo: TodoItem,
    onEdit: () -> Unit,
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

//@Composable
//fun LongPressDialog(
//    todo: TodoItem,
//    onEdit: () -> Unit,
//    viewModel: TodoViewModel,
//    onDelete: () -> Unit,
//    onDismiss: () -> Unit
//) {
//    AlertDialog(
//        onDismissRequest = onDismiss,
//        title = { Text(text = "Actions for ${todo.text}") },
//        text = { Text(text = "Choose an action") },
//        confirmButton = {
//            TextButton(onClick = {
////                onEdit()
////                onDismiss()
//                viewModel.showEditDialog(todo)
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
//
//}
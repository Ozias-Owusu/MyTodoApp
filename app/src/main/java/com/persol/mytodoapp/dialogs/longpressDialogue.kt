package com.persol.mytodoapp.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.persol.mytodoapp.screens.TodoItem
import com.persol.mytodoapp.screens.showDateTimePicker
import com.persol.mytodoapp.viewModels.TodoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LongPressDialog(
    todo: TodoItem,
    onEdit: (TodoItem) -> Unit,
    viewModel: TodoViewModel,
    onDelete: () -> Unit,
    onDismiss: () -> Unit,
    onShowEditDialog: () -> Unit
) {
    ModalBottomSheet(
        scrimColor = Color.Transparent,
        onDismissRequest = onDismiss,
        tonalElevation = 10.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Task Options",
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
                )
            Column (
                modifier = Modifier
                    .padding(
                        top = 20.dp,
                        bottom = 20.dp,
                        start = 30.dp,
                        end = 30.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .fillMaxWidth(),
                    onClick = {
                    onShowEditDialog()
                }) {
                    Text(text = "Edit")
                }
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                    onDelete()
                },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(text = "Delete")
                }
            }
        }
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
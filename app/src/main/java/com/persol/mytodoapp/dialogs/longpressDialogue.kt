package com.persol.mytodoapp.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.persol.mytodoapp.R
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
                        end = 30.dp
                    )
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditTodoDialog(
    todo: TodoItem,
    onEditTodo: (TodoItem) -> Unit,
    onDismiss: () -> Unit
) {
    var updatedTodo by remember { mutableStateOf(todo.text) }
    var updatedDateTime by remember { mutableStateOf(todo.dateTime) }

    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        scrimColor = Color.Transparent,
        tonalElevation = 10.dp,
        ) {
        Column (
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Edit Todo",
                fontSize = 30.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier.padding(bottom = 16.dp))
            OutlinedTextField(
                value = updatedTodo,
                onValueChange = { updatedTodo = it },
                label = { Text(text = "Todo Text") },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Required*",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Start
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row (
                modifier = Modifier.fillMaxWidth()

            ){
                InputChip(
                    onClick = {
                        showDateTimePicker(context, initialDateTime = updatedDateTime) { dateTime ->
                            updatedDateTime = dateTime
                        }
                    },
                    label = {
                        Text(
                            text = updatedDateTime.ifBlank
                            { "Select Date and Time" }
                        )
                    },
                    modifier = Modifier,
                    leadingIcon = {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = null
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                            contentDescription = "Calender"
                        )
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = InputChipDefaults.inputChipColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    selected = false
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 15.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Required*",
                    fontSize = 13.sp,
                    textAlign = TextAlign.Start
                )
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ){
                TextButton(
                    onClick = {
                    //                onEditTodo(todo.copy(text = updatedTodo))
                    //                onDismiss()
                    onEditTodo(todo.copy(text = updatedTodo, dateTime = updatedDateTime))
                    onDismiss()
                },
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                    ),
                    modifier = Modifier.width(100.dp)
                    ) {
                    Text("Save")
                }
            }
        }
    }
}
package com.persol.mytodoapp.details


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.persol.mytodoapp.R
import com.persol.mytodoapp.screens.TodoItem
import com.persol.mytodoapp.screens.showDateTimePicker


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoDetailsCard(
    modifier: Modifier = Modifier, initialTodo: TodoItem? = null,
    onAddTodo: (TodoItem) -> Unit, onCancel: () -> Unit, onUpdateTodo: (TodoItem) -> Unit,
) {
    var todoText by remember { mutableStateOf("") }
    var selectedDateTime by remember { mutableStateOf("") }
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf<String?>(null) }


    ModalBottomSheet(
        onDismissRequest = { onCancel() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        )
        {
            Text(
                text = "TODO DETAILS",
                fontSize = 25.sp,
                fontStyle = FontStyle.Italic
            )
            Spacer(modifier = Modifier.padding(5.dp))
            OutlinedTextField(
                value = todoText,
                shape = RoundedCornerShape(10.dp),
                onValueChange = {
                    todoText = it
                },
                label = { Text("Please enter todo") },
                supportingText = {Text(
                    text = "Required*"
                )},
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(10.dp))
            Row (
                modifier = modifier.fillMaxWidth()

            ){
                InputChip(
                    onClick = {
                        showDateTimePicker(context) { dateTime ->
                            selectedDateTime = dateTime
                        }
                    },
                    label = {
                        Text(
                            text = selectedDateTime.ifBlank
                            { "Select Date and Time" }
                        )
                    },
                    modifier = modifier,
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

            Spacer(modifier = Modifier.padding(10.dp))

            errorMessage?.let {
                Text(text = it, color = Color(0xFFFD4F4F), fontSize = 15.sp)
                Spacer(modifier = Modifier.padding(5.dp))
            }

            Row {
                Button(onClick = { onCancel() }) {
                    Text(text = "Cancel")
                }
                Spacer(modifier = Modifier.weight(1f))
                Button(onClick = {
                    if (todoText.isNotBlank() && selectedDateTime.isNotBlank()) {
                        if (initialTodo != null) {
                            // Update existing todo
                            onUpdateTodo(TodoItem(todoText, selectedDateTime, initialTodo.isCompleted.toString()))
                        } else {
                            // Add new todo
                            onAddTodo(
                                TodoItem(
                                todoText, selectedDateTime,
                                dateTime = selectedDateTime,
                                isCompleted = false
                            )
                            )
                        }
                    } else {
                        errorMessage = "Please fill all fields"
                    }
                }) {
                    Text(text = if (initialTodo != null) "Update Todo" else "Add Todo")
                }
            }

        }
    }
}

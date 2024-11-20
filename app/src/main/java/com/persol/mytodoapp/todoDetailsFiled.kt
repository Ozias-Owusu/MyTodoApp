package com.persol.mytodoapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TodoDetailsCard(
    modifier: Modifier = Modifier, initialTodo: TodoItem? = null,
    onAddTodo: (TodoItem) -> Unit, onCancel: () -> Unit
) {
    var todoText by remember { mutableStateOf(initialTodo?.text ?: "") }
    var selectedDateTime by remember { mutableStateOf(initialTodo?.dateTime ?: "") }
    val context = LocalContext.current
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val viewModel: TodoViewModel = viewModel()


    Card(
        modifier
            .padding(vertical = 40.dp)
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
                onValueChange = {
                    todoText = it
                },
                label = { Text("Please enter todo") },
            )
            Spacer(modifier = Modifier.padding(10.dp))
            OutlinedTextField(
                value = selectedDateTime,
                onValueChange = {
                    selectedDateTime = it
                },
                modifier= Modifier.clickable {
                    showDateTimePicker(context) { dateTime ->
                    selectedDateTime = dateTime
                }},
                label = { Text("Please select time") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = {
                        showDateTimePicker(context) { dateTime ->
                            selectedDateTime = dateTime
                        }

                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_calendar_today_24),
                            contentDescription = "Calender"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.padding(10.dp))

            errorMessage?.let {
                Text(text = it, color = Color(0xFFFD4F4F), fontSize = 20.sp)
                Spacer(modifier = Modifier.padding(5.dp))
            }

            Row {

                Button(onClick = {
                    if (todoText.isNotBlank() && selectedDateTime.isNotBlank()) {
                        if (initialTodo != null) {
                            val updatedTodo = initialTodo.copy(text = todoText, dateTime = selectedDateTime)
                            onAddTodo(updatedTodo)
                        } else {
                            onAddTodo(TodoItem(todoText, selectedDateTime))
                        }
                        todoText = ""
                        selectedDateTime = ""
                        onCancel()
                    } else {
                        errorMessage = "Please fill all fields."
                    }
                }) {
                    Text(text = if (initialTodo != null) "Update Todo" else "Add Todo")
                }
                Spacer(modifier = Modifier.width(25.dp))

                Button(onClick = { onCancel() }) {
                    Text(text = "Cancel")
                }
            }

        }
    }
}

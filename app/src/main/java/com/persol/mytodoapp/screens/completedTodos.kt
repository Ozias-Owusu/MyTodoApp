package com.persol.mytodoapp.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.persol.mytodoapp.R
import com.persol.mytodoapp.viewModels.TodoViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedTodos(
    viewModel: TodoViewModel,
    navController: NavHostController,
) {
    val completedTodos by remember { derivedStateOf { viewModel.completedTodoList } }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Completed Todos") },
                                navigationIcon = {
                    IconButton(onClick = { navController.navigate("homePage") }) {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_24),
                            contentDescription = "Menu"
                        )
                    }
                }

                )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (completedTodos.isEmpty()) {
                    Text(
                        text = "No completed todos.",
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    LazyColumn {
                        items(completedTodos) { todo ->
                            CompletedTodoItem(todo)
                        }
                    }
                }
            }
        },
        bottomBar = {
            Button(
                onClick = { viewModel.clearCompletedTodos() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Clear All")
            }
        }
    )
}

@Composable
fun CompletedTodoItem(todo: TodoItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = todo.text,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Text(
            text = "Completed: ${formatDate(todo.dateTime)}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
fun formatDate(timestamp: String): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

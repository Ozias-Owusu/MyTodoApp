//package com.persol.mytodoapp.screens
//
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material3.Button
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.text.style.TextDecoration
//import androidx.compose.ui.unit.dp
//import androidx.navigation.NavHostController
//
//@Composable
//fun CompletedTodos(navController: NavHostController, todoList: List<TodoItem>, onClearCompleted: () -> Unit) {
//    val completedTodos = todoList.filter { it.isCompleted }
//
//    val completedTodos = todoList.filter { it.isCompleted }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text(text = "Completed Todos") },
//                navigationIcon = {
//                    IconButton(onClick = { navController.navigateUp() }) {
//                        Icon(
//                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_back),
//                            contentDescription = "Back"
//                        )
//                    }
//                }
//            )
//        },
//    Box(modifier = Modifier.fillMaxSize()) {
//        // List of completed todos
//        LazyColumn(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp),
//            contentPadding = PaddingValues(bottom = 64.dp) // Add padding for the button
//        ) {
//            items(completedTodos) { todo ->
//                Text(
//                    text = todo.text,
//                    textDecoration = TextDecoration.LineThrough
//                )
//            }
//        }
//
//        // Clear button at the bottom
//        Button(
//            modifier = Modifier
//                .align(Alignment.BottomCenter)
//                .padding(16.dp),
//            onClick = onClearCompleted
//        ) {
//            Text(text = "Clear Completed")
//        }
//    }
//}

package com.persol.mytodoapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.persol.mytodoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedTodos(
    navController: NavHostController,
    todoList: List<TodoItem>,
    onClearCompleted: () -> Unit
) {
    val completedTodos = todoList.filter { it.isCompleted }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Completed Todos") },
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
        content = { padding ->
            Box(modifier = Modifier.fillMaxSize()) {
                // List of completed todos
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding), // Adjust padding to account for the AppBar
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 64.dp) // Add padding for the button
                ) {
                    items(completedTodos) { todo ->
                        Text(
                            text = todo.text,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                // Clear button at the bottom
                Button(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp),
                    onClick = onClearCompleted
                ) {
                    Text(text = "Clear Completed")
                }
            }
        }
    )
}

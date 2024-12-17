package com.persol.mytodoapp.screens

import android.annotation.SuppressLint
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.persol.mytodoapp.R
import com.persol.mytodoapp.dialogs.EditTodoDialog
import com.persol.mytodoapp.dialogs.LongPressDialog
import com.persol.mytodoapp.viewModels.TodoViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompletedTodos(
    viewModel: TodoViewModel,
    navController: NavHostController,
) {
    val completedTodos by remember { derivedStateOf { viewModel.completedTodoList } }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    var showOptions by remember { mutableStateOf(false) }
    var onShowEditDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var selectedTodo = TodoItem(id = 0, text = "", dateTime = "", isCompleted = false)
    var editingTodo by remember { mutableStateOf<TodoItem?>(null) }
    var showTodoDetails by remember { mutableStateOf(false) }
    var isCheckboxChecked by remember { mutableStateOf(false) }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
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
                    LazyColumn(
                        Modifier.fillMaxSize()
                    ) {
                        items(completedTodos) { todo ->
                            CompletedTodoItem(todo,
                                onLongPress = {
                                    showOptions = true
                                    selectedTodo = todo
                                },
                                onSwipeRight = {
                                    showDeleteConfirmation = true
                                    selectedTodo = todo
                                },
                                onChecked = {
                                    selectedTodo = todo
                                    isCheckboxChecked = !isCheckboxChecked
                                }
                            )
                        }
                    }
                }
            }
            if (showOptions) {
                LongPressDialog(
                    todo = selectedTodo,
                    viewModel = viewModel,
                    onEdit = {
                        editingTodo = selectedTodo.copy()
                        showTodoDetails = true
                    },
                    onDelete = {
                        showDeleteConfirmation = true
                        showOptions = false
                    },
                    onDismiss = {
                        showOptions = false
                    },
                    onShowEditDialog = {
                        showOptions = false
                        onShowEditDialog = true
                    }
                )
            }
            if (onShowEditDialog) {
                EditTodoDialog(
                    todo = selectedTodo,
                    onEditTodo = {
                        viewModel.updateTodo(selectedTodo, it)
                    },
                    onDismiss = {
                        onShowEditDialog = false
                    }
                )
            }

            if (showDeleteConfirmation) {
                AlertDialog(
                    onDismissRequest = { showDeleteConfirmation = false },
                    title = { Text("Delete Todo") },
                    text = { Text("Are you sure you want to delete this todo?") },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDeleteConfirmation = false
                                scope.launch {
                                    val success = viewModel.deleteTodo(selectedTodo)
                                    if (success) {
                                        snackbarHostState.showSnackbar("Todo successfully deleted!")
                                    } else {
                                        snackbarHostState.showSnackbar("Failed to delete the todo. Please try again.")
                                    }
                                }
                            }
                        ) {
                            Text("Delete")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showDeleteConfirmation = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
            if (isCheckboxChecked){
                AlertDialog(
                    onDismissRequest = { isCheckboxChecked = false },
                    title = { Text("Confirm Completion") },
                    text = {
                        Text(
                            "Are you sure you want to set the task: ${selectedTodo.text} as uncompleted?"
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            isCheckboxChecked = false
                            scope.launch {
                                viewModel.toggleTodo(selectedTodo)
                                snackbarHostState.showSnackbar(
                                    message = "Task Reversed Successfully"
                                )
                            }

                        }) {
                            Text("Confirm")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { isCheckboxChecked = false }) {
                            Text("Cancel")
                        }
                    }
                )
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

@SuppressLint("UseOfNonLambdaOffsetOverload")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CompletedTodoItem(
    todo: TodoItem,
    onLongPress: () -> Unit,
    onSwipeRight: () -> Unit,
    onChecked: () -> Unit,
) {
    var offsetX by remember { mutableFloatStateOf(0f) }

    SwipeToDismissBox(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .animateContentSize(
                animationSpec = tween(
                    durationMillis = 300,
                    easing = LinearOutSlowInEasing
                )
            )
            .background(
                if (offsetX > -10) {
                    Color.Red
                } else if (offsetX < 350) {
                    MaterialTheme.colorScheme.primaryContainer
                } else {
                    MaterialTheme.colorScheme.background
                },
                shape = RoundedCornerShape(
                    16.dp
                )
            ),
        backgroundContent = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (offsetX > 10) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.White,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }
                if (offsetX < -10) {
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.Check,
                        contentDescription = "Done",
                        tint = Color.White,
                        modifier = Modifier.padding(end = 10.dp)
                    )
                }
            }
        },
        state = rememberSwipeToDismissBoxState(),
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .offset(x = offsetX.dp)
                .offset { IntOffset(offsetX.roundToInt(), 0) }
                .pointerInput(Unit) {
                    detectHorizontalDragGestures(
                        onDragStart = {},
                        onDragEnd = {
                            if (offsetX > size.width / 2) {
                                onSwipeRight()
                            } else if (offsetX < -size.width / 2) {
                                onChecked()
                            }
                            offsetX = 0f
                        },
                        onHorizontalDrag = { change, dragAmount ->
                            change.consume()
                            offsetX += dragAmount
                        },
                        onDragCancel = {},
                    )
                }
                .combinedClickable(
                    onClick = {},
                    onDoubleClick = {},
                    onLongClick = onLongPress,
                ),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = todo.text,
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = "Due: ${formatDate(todo.dateTime)}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Checkbox(
                    checked = todo.isCompleted,
                    onCheckedChange = { onChecked() },
                    modifier = Modifier.align(Alignment.CenterVertically)
                        .padding(start = 10.dp)
                )
            }
        }
    }
}
fun formatDate(timestamp: String): String {
    val formatter = SimpleDateFormat("MMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
    return formatter.format(Date(timestamp))
}

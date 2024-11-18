package com.persol.mytodoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch

data class TodoItem(val text:String, val dateTime:String, var isCompleted: Boolean = false)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiUpdate(modifier: Modifier = Modifier, navController: NavController) {
    var showTodoDetails by remember { mutableStateOf(false) }
    var todoList by remember { mutableStateOf(listOf<TodoItem>()) }
    var selectedTodo by remember { mutableStateOf<TodoItem?>(null) }
    var editingTodo by remember { mutableStateOf<TodoItem?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                    Box(
                        modifier = Modifier
                            .size(80.dp) 
                            .clip(shape = CircleShape)
                            .background(Color(0xFF000000))
                            .clickable {  },
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = {}) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_person_24),
                                contentDescription = "Person",
                                modifier = Modifier.size(60.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Spacer(modifier = Modifier.height(16.dp))
                    Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center,
                        )
                    {
                        Text("User Name", modifier = Modifier.padding(bottom = 4.dp, top = 6.dp))
                        Text("Contact Info", fontSize = 12.sp)
                    }
                }

                HorizontalDivider()

                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {  }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_assignment_24),
                        contentDescription = "My Todos",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("My Todos")
                }
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {
                    navController.navigate("completedTodos_page")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_circle_24),
                        contentDescription = "Completed Todos",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Completed Todos")
                }

                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {  }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_loop_24),
                        contentDescription = "In-Progess Todos",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("In-Progess Todos")
                }

                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {  }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_settings_24),
                        contentDescription = "Settings",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Settings")
                }

            }
        }
    )
    {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Todo App",
                            textAlign = TextAlign.Center, fontSize = 30.sp
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_menu_24),
                                contentDescription = "Menu"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = {

                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.baseline_person_24),
                                contentDescription = "Person"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                )
            },

            ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (!showTodoDetails) {
                    if (todoList.isEmpty()) {
                        Column(
                            modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(text = "Tap on the + button to add tasks")
                        }
                    } else {
                        LazyColumn(
                            modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            reverseLayout = true,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        )
                        {
                            items(todoList) { todo ->
                                TodoItemCard(
                                    todo = todo,
                                    onLongPress = { selectedTodo = todo },
                                    onSwipeRight = {
                                        selectedTodo = todo
                                        showDeleteConfirmation = true
                                    }
                                )
                            }
                        }
                    }
                }

                FloatingActionButton(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(30.dp),
                    onClick = {
                        selectedTodo = null
                        showTodoDetails = true
                    },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_add_24),
                        contentDescription = "Add"
                    )
                }

                if (showTodoDetails) {
                    TodoDetailsCard(modifier = Modifier.align(Alignment.Center),
                        onAddTodo = { todo ->
                            if (editingTodo != null) {
                                todoList = todoList.map { if (it == editingTodo) todo else it }
                            } else {
                                todoList = todoList + todo
                            }
                            showTodoDetails = false
                            editingTodo = null
                        },
                        onCancel = { showTodoDetails = false }
                    )
                }
                selectedTodo?.let { todo ->
                    LongPressDialog(
                        todo = todo,
                        onEdit = {
                            editingTodo = todo
                            showTodoDetails = true
                            selectedTodo = null
                        },
                        onDismiss = {
                            selectedTodo = null
                        }
                    )
                }

                if (showDeleteConfirmation && selectedTodo != null) {
                    DeleteConfirmationDialog(
                        onConfirm = {
                            todoList = todoList - selectedTodo!!
                            selectedTodo = null
                            showDeleteConfirmation = false
                        },
                        onDismiss = {
                            showDeleteConfirmation = false
                        },
                        onDelete = {
                            todoList = todoList - selectedTodo!!
                            selectedTodo = null
                        }
                    )
                }
            }
        }
    }
}

    fun showDateTimePicker(context: Context, onDateTimeSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        val selectedDateTime = "$dayOfMonth/${month + 1}/$year  $hour:$minute"
                        onDateTimeSelected(selectedDateTime)
                    },
                    calendar.get(Calendar.HOUR_OF_DAY),
                    calendar.get(Calendar.MINUTE),
                    true
                ).show()
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }







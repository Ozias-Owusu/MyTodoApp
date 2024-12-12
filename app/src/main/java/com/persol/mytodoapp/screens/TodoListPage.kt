package com.persol.mytodoapp.screens

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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.persol.mytodoapp.dialogs.LongPressDialog
import com.persol.mytodoapp.R
import com.persol.mytodoapp.database.TodoDao
import com.persol.mytodoapp.details.TodoDetailsCard
import com.persol.mytodoapp.details.TodoItemCard
import com.persol.mytodoapp.dialogs.DeleteConfirmationDialog
import com.persol.mytodoapp.viewModels.TodoViewModel
import com.persol.mytodoapp.viewModels.TodoViewModelFactory
import kotlinx.coroutines.launch


@Entity(tableName = "todo_items")
data class TodoItem(@PrimaryKey(autoGenerate = true) val id: Int =0,
                    var text:String,
                    val dateTime:String,
                    var isCompleted: Boolean = false)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiUpdate(
    navController: NavController,
    todoDao: TodoDao,
    modifier: Modifier = Modifier,

    ) {
    var showTodoDetails by remember { mutableStateOf(false) }
//    var todoList by remember { mutableStateOf(listOf<TodoItem>()) }
    var selectedTodo by remember { mutableStateOf<TodoItem?>(null) }
    var editingTodo by remember { mutableStateOf<TodoItem?>(null) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
//    val viewModel: TodoViewModel = viewModel()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var completedTasks by remember { mutableStateOf(Pair(String, String)) }

    val viewModel: TodoViewModel = viewModel(factory = TodoViewModelFactory(todoDao))


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
                            .clickable {
                                navController.navigate("postsScreen")
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        IconButton(onClick = {
                            navController.navigate("postsScreen")
                        }) {
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
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {})
                {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_check_circle_24),
                        contentDescription = "Completed Todos",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Completed Todos")
                }

                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {

                    navController.navigate("postsScreen")

                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_person_24),
                        contentDescription = "People",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Posts")
                }
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {})
                {
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
                        })
                        {
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
                    if (viewModel.todoList.isEmpty()) {
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
                            items(viewModel.todoList) { todo ->
                                TodoItemCard(
                                    todo = todo,
                                    onLongPress = { selectedTodo = todo },
                                    onSwipeRight = {
                                        selectedTodo = todo
                                        showDeleteConfirmation = true
                                    },
                                    viewModel = viewModel,
                                    onChecked = {}
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
                    TodoDetailsCard(
                        modifier = Modifier.align(Alignment.Center),
                        initialTodo = editingTodo,
                        onAddTodo = { newTodo ->
                            if (editingTodo != null) {
                                viewModel.updateTodo(editingTodo!!, newTodo)
                            } else {
                                viewModel.addTodo(newTodo)
                            }
                            showTodoDetails = false
                            editingTodo = null
                        },
                        onCancel = { showTodoDetails = false },
                        onUpdateTodo = {}
                    )
                }
                selectedTodo?.let { todo ->
                    LongPressDialog(
                        todo = todo,
                        viewModel = viewModel,
                        onEdit = {
                            editingTodo = todo.copy()
                            showTodoDetails = true
                            selectedTodo = null
                        },
                        onDelete = {
                            viewModel.deleteTodo(todo)
                        },
                        onDismiss = {
                            selectedTodo = null
                        }
                    )
                }
                if (showDeleteConfirmation && selectedTodo != null) {
                    DeleteConfirmationDialog(
                        onConfirm = {
                            viewModel.deleteTodo(selectedTodo!!)
                            selectedTodo = null
                            showDeleteConfirmation = false
                        },
                        onDismiss = {
                            showDeleteConfirmation = false
                        },
                        onDelete = {
                            viewModel.deleteTodo(selectedTodo!!)
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


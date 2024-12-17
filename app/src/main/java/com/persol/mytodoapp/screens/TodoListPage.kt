package com.persol.mytodoapp.screens

import android.annotation.SuppressLint
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
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDrawerState


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.persol.mytodoapp.dialogs.EditTodoDialog
//import com.persol.mytodoapp.dialogs.DeleteConfirmationDialog
import com.persol.mytodoapp.viewModels.TodoViewModel
import com.persol.mytodoapp.viewModels.TodoViewModelFactory
import com.persol.mytodoapp.workers.scheduleTodoNotification
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Entity(tableName = "todo_items")
data class TodoItem(@PrimaryKey(autoGenerate = true)
                    val id: Int = 0,
                    var text:String,
                    val dateTime:String,
                    var isCompleted: Boolean = false,
)


@SuppressLint("RememberReturnType")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiUpdate(
    navController: NavController,
    todoDao: TodoDao,
    modifier: Modifier = Modifier,

    ) {
    val coroutineScope = rememberCoroutineScope()
    var showTodoDetails by rememberSaveable { mutableStateOf(false) }
    var selectedTodo = TodoItem(id = 0, text = "", dateTime = "", isCompleted = false)
    var editingTodo by rememberSaveable { mutableStateOf<TodoItem?>(null) }
    var showDeleteConfirmation by rememberSaveable { mutableStateOf(false) }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val viewModel: TodoViewModel = viewModel(factory = TodoViewModelFactory(todoDao))
    var showCompleteDeleteConfirmation by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var isCheckboxChecked by rememberSaveable  { mutableStateOf(false) }
    var showOptions by rememberSaveable { mutableStateOf(false) }
    var onShowEditDialog by rememberSaveable { mutableStateOf(false) }



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
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {
                    navController.navigate("homePage")
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_assignment_24),
                        contentDescription = "My Todos",
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("My Todos")
                }
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {
                    navController.navigate("completedTodosPage")
                })
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
            },    snackbarHost = { SnackbarHost(snackbarHostState) }
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
                                    onLongPress = {
                                        selectedTodo = todo
                                        showOptions = true
                                        println(selectedTodo.toString())
                                                  },
                                    onSwipeRight = {
                                        selectedTodo = todo
                                        showDeleteConfirmation = true
                                    },
                                    viewModel = viewModel,
                                    onChecked = {
                                        selectedTodo = todo
                                        isCheckboxChecked = true
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
                            scope.launch {
                                val success = viewModel.addTodo(newTodo)
                                if (success) {
                                    //convert date to time in milliseconds format
                                    val format = "dd/MM/yyyy  HH:mm"
                                    val date = convertDateStringToMilliseconds(newTodo.dateTime, format)
                                    println(date)
                                    println(System.currentTimeMillis())

                                    scheduleTodoNotification(context = context, newTodo.text, date)
                                    snackbarHostState.showSnackbar("Todo successfully added!")
                                } else {
                                    snackbarHostState.showSnackbar("Todo could not be added, please try again.")
                                }
                            }
                            if (editingTodo != null) {
                                viewModel.updateTodo(editingTodo!!, newTodo)

                            }
                            showTodoDetails = false
                            editingTodo = null
                        },
                        onCancel = { showTodoDetails = false },
                        onUpdateTodo = {}
                    )
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

                if (showCompleteDeleteConfirmation ) {
                    AlertDialog(
                        onDismissRequest = { showCompleteDeleteConfirmation = false },
                        title = { Text("Confirm Complete Deletion") },
                        text = { Text("Are you sure you want to delete this todo completely? This action cannot be undone.") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    coroutineScope.launch {
                                        val success = viewModel.deleteTodo(selectedTodo)
                                        if (success) {
                                            snackbarHostState.showSnackbar("Todo successfully deleted!")
                                        } else {
                                            snackbarHostState.showSnackbar("Failed to delete the todo. Please try again.")
                                        }
                                        showCompleteDeleteConfirmation = false
                                    }
                                }
                            ) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { showCompleteDeleteConfirmation = false }
                            ) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }
            if (isCheckboxChecked){
                AlertDialog(
                    onDismissRequest = { isCheckboxChecked = false },
                    title = { Text("Confirm Completion") },
                    text = {
                        Text(
                            "Are you sure you want to set the task: ${selectedTodo.text} as completed?"
                        )
                    },
                    confirmButton = {
                        TextButton(onClick = {
                            isCheckboxChecked = false
                            scope.launch {
                                viewModel.toggleTodo(selectedTodo)
                                snackbarHostState.showSnackbar(
                                    message = "Task Cmpleted Successfully"
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
        }
    }
}
fun showDateTimePicker(context: Context,initialDateTime: String, onDateTimeSelected: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())

    try {
        calendar.time = sdf.parse(initialDateTime) ?: Date()
    } catch (e: Exception) {
        calendar.time = Date()
    }
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

fun convertDateStringToMilliseconds(dateString: String, format: String): Long {
    val dateFormat = SimpleDateFormat(format, Locale.getDefault())
    val date = dateFormat.parse(dateString)
    return date?.time ?: 0 // Handle null case with 0 or throw an exception
}


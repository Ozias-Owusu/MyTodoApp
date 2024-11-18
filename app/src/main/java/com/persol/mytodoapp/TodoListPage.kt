package com.persol.mytodoapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.icu.util.Calendar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState


import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

import com.google.firestore.v1.Value

data class TodoItem(val text:String, val dateTime:String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UiUpdate(modifier: Modifier = Modifier) {
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
                            .clickable {  }
                        ,
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
                Row(modifier = Modifier.fillMaxWidth().padding(16.dp).clickable {  }) {
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

    @Composable
    fun TodoDetailsCard(
        modifier: Modifier = Modifier, initialTodo: TodoItem? = null,
        onAddTodo: (TodoItem) -> Unit, onCancel: () -> Unit
    ) {
        var todoText by remember { mutableStateOf("") }
        var selectedDateTime by remember { mutableStateOf("") }
        val context = LocalContext.current
        var errorMessage by remember { mutableStateOf<String?>(null) }


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
                    onValueChange = {},
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
                            onAddTodo(TodoItem(todoText, selectedDateTime))
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

    @Composable
    fun TodoItemCard(todo: TodoItem, onLongPress: () -> Unit, onSwipeRight: () -> Unit) {
        var offsetX by remember { mutableFloatStateOf(0f) }
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .offset(x = offsetX.dp) // Apply the offset for visual movement
                .draggable(
                    state = rememberDraggableState { delta ->
                        offsetX += delta // Update offset as the user drags
                    },
                    orientation = Orientation.Horizontal,
                    onDragStopped = { velocity ->
                        if (velocity > 1000 || offsetX > 100) {  // Swiping right
                            onSwipeRight()
                        }
                        offsetX = 0f // Reset offset after swipe
                    }
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = { onLongPress() }
                    )
                }


        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "Todo: ${todo.text}", fontSize = 18.sp)
                Text(text = "Due: ${todo.dateTime}", fontSize = 14.sp)
            }
        }
    }


    @Composable
    fun LongPressDialog(
        todo: TodoItem,
        onEdit: () -> Unit,
//    onDelete: () -> Unit,
        onDismiss: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Actions for ${todo.text}") },
            text = { Text(text = "Choose an action") },
            confirmButton = {
                TextButton(onClick = {
                    onEdit()
                    onDismiss()
                }) {
                    Text("Edit")
                }
            },
            dismissButton = {
                TextButton(onClick = {
//                onDelete()
                    onDismiss()
                }) {
                    Text("Cancel")
                }
            }
        )
    }

    @Composable
    fun DeleteConfirmationDialog(
        onConfirm: () -> Unit,
        onDismiss: () -> Unit,
        onDelete: () -> Unit
    ) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = "Delete Todo") },
            text = { Text(text = "Are you sure you want to delete this todo?") },
            confirmButton = {
                TextButton(onClick = {
                    onDismiss()
                }) {
                    Text("Cancel")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDelete()
                    onDismiss()
                }) {
                    Text("Delete")
                }
            }
        )
    }


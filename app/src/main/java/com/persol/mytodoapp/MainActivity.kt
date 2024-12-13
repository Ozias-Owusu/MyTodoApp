package com.persol.mytodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.persol.mytodoapp.appmodule.AppModule
import com.persol.mytodoapp.screens.CompletedTodos
import com.persol.mytodoapp.screens.PostScreen
import com.persol.mytodoapp.screens.Settings
import com.persol.mytodoapp.screens.UiUpdate
import com.persol.mytodoapp.viewModels.TodoViewModel
import com.persol.mytodoapp.viewModels.TodoViewModelFactory

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: TodoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val database = AppModule.provideDatabase(this)
            val todoDao = database.todoDao()

            // Initialize ViewModel
            viewModel = ViewModelProvider(
                this,
                TodoViewModelFactory(todoDao)
            )[TodoViewModel::class.java]

            val navController = rememberNavController()


            NavHost(
                navController = navController,
                startDestination = "homePage",
                builder =
                {
                    composable("homePage") {
                        UiUpdate(
                            todoDao = todoDao,
                            navController = navController,
                            modifier = Modifier
                        )
                    }
                    composable("completedTodosPage") {
                        CompletedTodos(navController,onClearCompleted = {}, todoList = listOf())
                    }
                    composable("settingsPage") {
                        Settings(navController)
                    }
                    composable("postsScreen") {
                        PostScreen(navController)
                    }

                }
            )
        }
    }
}



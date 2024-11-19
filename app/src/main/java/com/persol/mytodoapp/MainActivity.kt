package com.persol.mytodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = "home_page",
                builder =
                {
                    composable("home_page") {
                        UiUpdate(navController)
                    }
                    composable("completedTodos_page") {
                       CompletedTodos(todoList = listOf())
                    }
                    composable("in_progressTodos_page") {
                        InprogressTodo()

                    }
                    composable("settings_page") {
                        Settings()
                    }

                }
            )
        }
    }
}

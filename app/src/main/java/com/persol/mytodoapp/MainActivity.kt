package com.persol.mytodoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
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
                startDestination = "homePage",
                builder =
                {
                    composable("homePage") {
                        UiUpdate(navController)
                    }
                    composable("completedTodosPage") {
                        CompletedTodos(navController,todoList = listOf())
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



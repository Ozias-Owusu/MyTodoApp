package com.persol.mytodoapp

import com.persol.mytodoapp.Screens.CompletedTodos
import com.persol.mytodoapp.Screens.PostScreen
import com.persol.mytodoapp.Screens.Settings
import com.persol.mytodoapp.Screens.UiUpdate
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



package com.persol.mytodoapp

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.persol.mytodoapp.appmodule.AppModule
import com.persol.mytodoapp.screens.CompletedTodos
import com.persol.mytodoapp.screens.PostScreen
import com.persol.mytodoapp.screens.Settings
import com.persol.mytodoapp.screens.UiUpdate
import com.persol.mytodoapp.ui.theme.MyTodoAppTheme
import com.persol.mytodoapp.viewModels.TodoViewModel
import com.persol.mytodoapp.viewModels.TodoViewModelFactory
import com.persol.mytodoapp.workers.showNotification

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: TodoViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            if (!isNotificationPermissionGranted(this)) {
                requestNotificationPermission(this)
            }
            val database = AppModule.provideDatabase(this)
            val todoDao = database.todoDao()

            viewModel = ViewModelProvider(
                this,
                TodoViewModelFactory(todoDao)
            )[TodoViewModel::class.java]

            val navController = rememberNavController()

            MyTodoAppTheme {
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
                            CompletedTodos(viewModel = viewModel, navController)
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
    // Create the notification channel
    private fun createNotificationChannel () {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "todo_channel_id",
                "Todo Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
    }
    //PERMISSION HANDLERS
    @SuppressLint("InlinedApi")
    private fun isNotificationPermissionGranted(context: Context): Boolean {
        return ActivityCompat
            .checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("InlinedApi")
    private fun requestNotificationPermission(activity: Activity) {
        if (!isNotificationPermissionGranted(activity)) {
            requestPermissionLauncher.launch(
                arrayOf(
                    android.Manifest.permission.POST_NOTIFICATIONS,
                )
            )
        }
    }

    private val requestPermissionLauncher =
        super.registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            permissions.entries.forEach {
                val permissionName = it.key
                val isGranted = it.value
                if (isGranted) {
                    // Permission is granted. Continue the action or workflow in your app.
                    createNotificationChannel()
                } else {
                    // Explain to the user that the feature is unavailable without this permission.
                    AlertDialog.Builder(this)
                        .setTitle("Permission Required")
                        .setMessage("This feature requires notification permission to function. Please grant it in settings.")
                        .setPositiveButton("Go to Settings") { _, _ ->
                            // Open app settings for the user to grant permission
                            val intent =
                                Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                            val uri = Uri.fromParts("package", this.packageName, null)
                            intent.data = uri
                            this.startActivity(intent)
                        }
                        .setNegativeButton("Cancel", null)
                        .show()
                }
            }
        }
}



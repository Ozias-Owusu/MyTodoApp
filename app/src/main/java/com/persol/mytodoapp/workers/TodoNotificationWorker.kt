package com.persol.mytodoapp.workers

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.WorkerParameters
import android.app.NotificationManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import com.persol.mytodoapp.R
import com.persol.mytodoapp.appmodule.AppModule
import com.persol.mytodoapp.database.TodoDatabase
import com.persol.mytodoapp.screens.TodoItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class TodoNotificationWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {
   override suspend fun doWork(): Result {
       val taskId = inputData.getString("todo_text")
       val todoTime = inputData.getLong("todo_time", 0)
        return withContext(Dispatchers.IO) {
            return@withContext try {
                showNotification(applicationContext, taskId.toString(), todoTime.toString())
                Log.d("TodoNotificationWorker", "Notification scheduled for $todoTime")
                Result.success()
            } catch (e: Exception) {
                Log.e("TodoNotificationWorker", "Error checking time: ${e.message}")
                Result.failure()
            }
        }
    }
}

@SuppressLint("NotificationPermission")
fun showNotification(context: Context, todoText: String, todoTime: String) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val notification = NotificationCompat.Builder(context, "todo_channel_id")
        .setContentTitle("Todo Reminder")
        .setContentText("Task labelled $todoText is due now!")
        .setSmallIcon(R.drawable.baseline_assignment_24)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()
    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}
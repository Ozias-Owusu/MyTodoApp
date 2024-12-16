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
       val todoText = inputData.getString("todo_text") ?: return Result.failure()
       val todoTime = inputData.getString("todo_time") ?: return Result.failure()
        return withContext(Dispatchers.IO) {
            return@withContext try {
                checkTime(todoTime)
                { showNotification(applicationContext, todoText, todoTime) }
                Log.d("TodoNotificationWorker", "Notification scheduled for $todoTime")
                Result.success()
            } catch (e: Exception) {
                Log.e("TodoNotificationWorker", "Error checking time: ${e.message}")
                Result.failure()
            }
        }
//        showNotification(applicationContext, todoText, todoTime)
//        return Result.success()
    }

}
fun checkTime( dateTime: String, run: () -> Unit) {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val targetTime = sdf.parse(dateTime)?.time ?: return
    val currentTime = Calendar.getInstance().timeInMillis
    val delay = targetTime - currentTime
    if (delay >= 0) {
        run()
        println("EURIKA!!!!!!!")
    } else {
        println("ANNFA WAI")
    }
}
@SuppressLint("NotificationPermission")
fun showNotification(context: Context, todoText: String, todoTime: String) {
    val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        val channelId = "todo_channel_id"
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val channel = NotificationChannel(
//                channelId,
//                "Todo Notifications",
//                NotificationManager.IMPORTANCE_DEFAULT
//            )
//            notificationManager.createNotificationChannel(channel)
//        }

    val notification = NotificationCompat.Builder(context, "todo_channel_id")
        .setContentTitle("Todo Reminder")
        .setContentText("$todoText - $todoTime")
        .setSmallIcon(R.drawable.baseline_assignment_24)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .build()
    notificationManager.notify(System.currentTimeMillis().toInt(), notification)
}
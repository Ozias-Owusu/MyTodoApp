package com.persol.mytodoapp.workers

import android.annotation.SuppressLint
import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.persol.mytodoapp.R

class TodoNotificationWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val todoText = inputData.getString("todo_text") ?: "You have a task to complete!"
        val todoTime = inputData.getString("todo_time") ?: "Now"

        showNotification(todoText, todoTime)

        return Result.success()
    }

    @SuppressLint("NotificationPermission")
    private fun showNotification(todoText: String, todoTime: String) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "todo_channel_id"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Todo Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("Todo Reminder")
            .setContentText("$todoText - $todoTime")
            .setSmallIcon(R.drawable.baseline_assignment_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .build()

        notificationManager.notify(System.currentTimeMillis().toInt(), notification)
    }
}
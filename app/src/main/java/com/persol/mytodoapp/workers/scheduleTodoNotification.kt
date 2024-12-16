package com.persol.mytodoapp.workers

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

fun scheduleTodoNotification(context: Context, todoText: String, dateTime: String) {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    val targetTime = sdf.parse(dateTime)?.time ?: return
    val currentTime = Calendar.getInstance().timeInMillis
    val delay = targetTime - currentTime

    if (delay >= 0) { // Only schedule if the time is in the future
        val inputData = Data.Builder()
            .putString("todo_text", todoText)
            .putString("todo_time", dateTime)
            .build()

        val workRequest = OneTimeWorkRequestBuilder<TodoNotificationWorker>()
            .setInputData(inputData)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
    }
}
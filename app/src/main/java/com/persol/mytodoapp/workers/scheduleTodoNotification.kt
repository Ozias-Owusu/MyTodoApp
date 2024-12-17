package com.persol.mytodoapp.workers

import android.content.Context
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit


fun scheduleTodoNotification(context: Context, todoText: String, dateTime: Long) {
        val inputData = Data.Builder()
            .putString("todo_text", todoText)
            .putLong("todo_time", dateTime)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<TodoNotificationWorker>()
            .setInitialDelay(dateTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            "todo_notification_$todoText",
            ExistingWorkPolicy.APPEND,
            workRequest
        )
}
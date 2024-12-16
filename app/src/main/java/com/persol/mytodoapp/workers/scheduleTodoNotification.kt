package com.persol.mytodoapp.workers

import android.content.Context
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager


fun scheduleTodoNotification(context: Context, todoText: String, dateTime: String) {
        val inputData = Data.Builder()
            .putString("todo_text", todoText)
            .putString("todo_time", dateTime)
            .build()
        val workRequest = OneTimeWorkRequestBuilder<TodoNotificationWorker>()
            .setInputData(inputData)
            .build()
        WorkManager.getInstance(context).enqueue(workRequest)
}
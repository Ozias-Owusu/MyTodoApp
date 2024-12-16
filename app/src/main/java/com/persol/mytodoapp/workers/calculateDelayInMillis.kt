package com.persol.mytodoapp.workers

import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

fun calculateDelayInMillis(dateTime: String): Long {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return try {
        val todoTime = sdf.parse(dateTime)?.time ?: 0
        val currentTime = System.currentTimeMillis()
        if (todoTime > currentTime) {
            todoTime - currentTime
        } else {
            0L // If time has already passed, return 0
        }
    } catch (e: Exception) {
        e.printStackTrace()
        0L
    }
}

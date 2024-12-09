package com.persol.mytodoapp

import android.app.Application
import androidx.room.Room
import com.persol.mytodoapp.databaseDetails.TodoDatabase

class MyApplication : Application() {
    val database: TodoDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            TodoDatabase::class.java,
            "todo_database"
        ).build()
    }
}
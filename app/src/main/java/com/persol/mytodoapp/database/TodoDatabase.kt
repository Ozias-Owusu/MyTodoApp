package com.persol.mytodoapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.persol.mytodoapp.screens.TodoItem

@Database(entities = [TodoItem::class], version = 1, exportSchema = false)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}

package com.persol.mytodoapp.appmodule

import android.content.Context
import androidx.room.Room
import com.persol.mytodoapp.database.TodoDatabase

object AppModule {
    private var INSTANCE: TodoDatabase? = null

    fun provideDatabase(context: Context): TodoDatabase {
        if (INSTANCE == null) {
            synchronized(TodoDatabase::class) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    TodoDatabase::class.java,
                    "todo_database"
                ).build()
            }
        }
        return INSTANCE!!
    }
}

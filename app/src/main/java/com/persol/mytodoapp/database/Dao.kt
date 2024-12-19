package com.persol.mytodoapp.database

import androidx.room.*
import androidx.room.Dao
import com.persol.mytodoapp.screens.TodoItem
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTodo(todo: TodoItem)

    @Update
    suspend fun updateTodo(todo: TodoItem)

    @Delete
    suspend fun deleteTodo(todo: TodoItem)

    @Query("SELECT * FROM todo_items ORDER BY id DESC")
    fun getAllTodos(): Flow<List<TodoItem>>

    @Query("DELETE FROM todo_items WHERE isCompleted = 1")
    suspend fun deleteAllCompletedTodos()

    //get dateTime
    @Query("SELECT * FROM todo_items WHERE dateTime = :dateTime")
    suspend fun getTodosByDate(dateTime: String): List<TodoItem>

    //delete all uncompleted todos
    @Query("DELETE FROM todo_items WHERE isCompleted = 0")
    suspend fun deleteAllTodos()


}
package com.example.zuper_todo.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.zuper_todo.models.Data

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(data: Data):Long

    @Query("SELECT * FROM todos")
    fun getAllTodos(): LiveData<List<Data>>

    //Delete query here if required
}
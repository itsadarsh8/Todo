package com.example.zuper_todo.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zuper_todo.models.Data

@Database(
    entities = [Data::class],
    version = 1
)
abstract class TodoDatabase : RoomDatabase() {

    abstract fun getTodoDao(): TodoDao

    companion object{
        private var instance: TodoDatabase?=null
        private val LOCK=Any()

        operator fun invoke(context: Context)= instance ?: synchronized(LOCK){

            //create database if instance null
            instance?:createDatabase(context).also{ instance=it}
        }
        private fun createDatabase(context: Context)=
            Room.databaseBuilder(
                context.applicationContext,
                TodoDatabase::class.java,
                "todo_db.db"
            ).build()
    }
}
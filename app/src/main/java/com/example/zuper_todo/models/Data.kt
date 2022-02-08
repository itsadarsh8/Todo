package com.example.zuper_todo.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//Setting room
@Entity(
    tableName="todos"
)

data class Data(
    @PrimaryKey
    val id:Int,
    val author: String,
    var is_completed: Boolean,
    val priority: String,
    val tag: String,
    val title: String
)
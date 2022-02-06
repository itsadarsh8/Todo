package com.example.zuper_todo.models

import com.example.zuper_todo.models.Data

data class TodoResponse(
    val data: MutableList<Data>,
    val total_records: Int
)
package com.example.zuper_todo.repository

import com.example.zuper_todo.api.RetrofitInstance
import com.example.zuper_todo.db.TodoDatabase

class TodoRepository(
    val db: TodoDatabase
) {
        suspend fun getTodos(pageNumber: Int, limit: Int, author: String)=
            RetrofitInstance.api.getTodos(pageNumber,limit,author)

    suspend fun searchTodo(pageNumber: Int, limit: Int,author: String,searchQuery: String)=
        RetrofitInstance.api.searchForTodo(pageNumber,limit,author,searchQuery)
}
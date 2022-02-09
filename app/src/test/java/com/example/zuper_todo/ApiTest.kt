package com.example.zuper_todo

import com.example.zuper_todo.api.RetrofitInstance
import com.example.zuper_todo.models.TodoResponse
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ApiTest {


    @Test
    fun fetchAllTodoTest ()= runBlocking{
        val todoList: TodoResponse? = RetrofitInstance.api.getAllTodos(1,1500,"Adarsh").body()
        Assert.assertNotEquals(0,todoList?.total_records)
    }

    @Test
    fun searchForTodoTest ()= runBlocking{
        val todoList: TodoResponse? = RetrofitInstance.api.searchForTodo(1,1500,"Adarsh","Android").body()
        Assert.assertNotEquals(0,todoList?.total_records)
    }

    @Test
    fun fetchTodoTest ()= runBlocking{
        val todoList: TodoResponse? = RetrofitInstance.api.getTodos(1,15,"Adarsh").body()
        Assert.assertNotEquals(0,todoList?.total_records)
    }
}
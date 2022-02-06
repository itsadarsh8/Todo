package com.example.zuper_todo.api

import com.example.zuper_todo.models.TodoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TodoAPI {

    @GET ("todo")
    suspend fun getTodos(

        @Query("_page") pageNumber:Int =1,
        @Query("_limit") limit:Int= 15,
        @Query("author") author:String= "Ranjith"

    ):Response<TodoResponse>

    @GET ("todo")
    suspend fun searchForTodo(

        @Query("_page") pageNumber:Int =1,
        @Query("_limit") limit:Int= 15,
        @Query("author") author:String= "Ranjith",
        @Query("tag") tag:String

    ):Response<TodoResponse>

}
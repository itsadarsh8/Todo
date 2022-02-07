package com.example.zuper_todo.api

import com.example.zuper_todo.models.BaseResponse
import com.example.zuper_todo.models.TodoInfoData
import com.example.zuper_todo.models.TodoResponse
import com.example.zuper_todo.utils.Constants.AUTHOR_NAME
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface TodoAPI {

    @GET ("todo")
    suspend fun getTodos(

        @Query("_page") pageNumber:Int =1,
        @Query("_limit") limit:Int= 15,
        @Query("author") author:String= AUTHOR_NAME

    ):Response<TodoResponse>

    @GET ("todo")
    suspend fun searchForTodo(

        @Query("_page") pageNumber:Int =1,
        @Query("_limit") limit:Int= 15,
        @Query("author") author:String= AUTHOR_NAME,
        @Query("tag") tag:String

    ):Response<TodoResponse>


    @POST ("todo")
    fun createTodo(@Body todoData:TodoInfoData): Call<TodoInfoData>

}
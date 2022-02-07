package com.example.zuper_todo.api

import com.example.zuper_todo.models.TodoInfoData
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.Retrofit

class RestApiService {
     fun createTodo(todoInfoData: TodoInfoData, onResult:(TodoInfoData?)->Unit){
        val retrofit= RetrofitInstance.api
        retrofit.createTodo(todoInfoData).enqueue(
            object :Callback<TodoInfoData>{
                override fun onFailure(call: Call<TodoInfoData>, t:Throwable){
                    onResult(null)
                }
                override fun onResponse(call:Call<TodoInfoData>,response:Response<TodoInfoData>){
                    val createNewTodo=response.body()
                    onResult(createNewTodo)
                }
            }
        )
    }
}



package com.example.zuper_todo

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.*
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zuper_todo.models.TodoResponse
import com.example.zuper_todo.repository.TodoRepository
import com.example.zuper_todo.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

class TodoViewModel(
    //To use application context in ViewModel
    app:Application,
    val todoRepository: TodoRepository
): AndroidViewModel(app) {

    val todoLiveData:MutableLiveData<Resource<TodoResponse>> = MutableLiveData()
    var todoPage = 1
    var limit = 15
    var todoResponse: TodoResponse?=null

    val searchTodoLiveData:MutableLiveData<Resource<TodoResponse>> = MutableLiveData()
    var searchTodoPage = 1
    var searchLimit = 1500
    var searchAuthor = "Ranjith"
    var searchTodoResponse: TodoResponse?=null

    init{
        getTodo(author = "Ranjith")
    }

    fun getTodo(author: String) = viewModelScope.launch {
        safeTodoCall(author)

    }

    fun searchTodo(searchQuery: String) = viewModelScope.launch {
        safeSearchTodoCall(searchQuery)

    }


    private fun handleTodoResponse(response: Response<TodoResponse>):Resource<TodoResponse>{
        if(response.isSuccessful){
            response.body()?.let{resultResponse->
                todoPage++
                if(todoResponse==null){
                    todoResponse=resultResponse
                }else{
                    val oldTodo=todoResponse?.data
                    val newTodo=resultResponse.data
                    oldTodo?.addAll(newTodo)
                }
                return Resource.Success(todoResponse?:resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private fun handleSearchTodoResponse(response: Response<TodoResponse>):Resource<TodoResponse>{
        if(response.isSuccessful){
            response.body()?.let{resultResponse->
                return Resource.Success(resultResponse)
            }
        }
        return Resource.Error(response.message())
    }

    private suspend fun safeSearchTodoCall(searchQuery: String){
        searchTodoLiveData.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()) {
                val response= todoRepository.searchTodo(searchTodoPage,searchLimit,searchAuthor, searchQuery)
                todoLiveData.postValue(handleSearchTodoResponse(response))
            }else{
                todoLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable){
            when(t){
                is IOException -> todoLiveData.postValue(Resource.Error("Network Failure"))
                else -> todoLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private suspend fun safeTodoCall(author: String){
        todoLiveData.postValue(Resource.Loading())
        try{
            if(hasInternetConnection()) {
                val response = todoRepository.getTodos(todoPage, limit, author)
                todoLiveData.postValue(handleTodoResponse(response))
            }else{
                todoLiveData.postValue(Resource.Error("No internet connection"))
            }
        } catch(t: Throwable){
            when(t){
                is IOException -> todoLiveData.postValue(Resource.Error("Network Failure"))
                else -> todoLiveData.postValue(Resource.Error("Conversion Error"))
            }
        }
    }

    private fun hasInternetConnection():Boolean{
        val connectivityManager=getApplication<TodoApplication>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        )as ConnectivityManager

        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            val activeNetwork= connectivityManager.activeNetwork?:return false
            val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork)?:return false
            return when{
                capabilities.hasTransport(TRANSPORT_WIFI)-> true
                capabilities.hasTransport(TRANSPORT_CELLULAR)-> true
                capabilities.hasTransport(TRANSPORT_ETHERNET)-> true
                else-> false
            }
        } else{
            connectivityManager.activeNetworkInfo?.run{
                return when(type){
                    TYPE_WIFI -> true
                    TYPE_MOBILE -> true
                    TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}

























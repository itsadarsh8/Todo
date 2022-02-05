package com.example.zuper_todo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.zuper_todo.models.TodoResponse
import com.example.zuper_todo.repository.TodoRepository
import com.example.zuper_todo.utils.Resource
import kotlinx.coroutines.launch
import retrofit2.Response

class TodoViewModel(
    val todoRepository: TodoRepository
): ViewModel() {

    val todoLiveData:MutableLiveData<Resource<TodoResponse>> = MutableLiveData()
    var todoPage = 1
    var limit = 15

    val searchTodoLiveData:MutableLiveData<Resource<TodoResponse>> = MutableLiveData()
    var searchTodoPage = 1
    var searchLimit = 15

    init{
        getTodo(author = "Ranjith")
    }

    fun getTodo(author: String) = viewModelScope.launch {
        todoLiveData.postValue(Resource.Loading())
        val response= todoRepository.getTodos(todoPage,limit,author)
        todoLiveData.postValue(handleTodoResponse(response))
    }

    fun searchTodo(searchQuery: String) = viewModelScope.launch {
        searchTodoLiveData.postValue(Resource.Loading())
        val response= todoRepository.searchTodo(todoPage,limit, searchQuery)
        todoLiveData.postValue(handleTodoResponse(response))
    }


    private fun handleTodoResponse(response: Response<TodoResponse>):Resource<TodoResponse>{
        if(response.isSuccessful){
            response.body()?.let{resultResponse->
                return Resource.Success(resultResponse)
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
}
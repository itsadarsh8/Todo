package com.example.zuper_todo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zuper_todo.repository.TodoRepository

class TodoViewModelProviderFactory(
    val todoRepository: TodoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return TodoViewModel(todoRepository) as T
    }
}
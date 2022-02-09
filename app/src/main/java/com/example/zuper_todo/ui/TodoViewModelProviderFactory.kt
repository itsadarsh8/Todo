package com.example.zuper_todo.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.zuper_todo.repository.TodoRepository

class TodoViewModelProviderFactory(
    val app:Application,
    val todoRepository: TodoRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
       return TodoViewModel(app, todoRepository) as T
    }
}
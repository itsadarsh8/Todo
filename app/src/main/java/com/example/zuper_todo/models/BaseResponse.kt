package com.example.zuper_todo.models

class BaseResponse<T> (
    var code:Int,
    var message: String?,
    var data: T?
    )
package com.example.zuper_todo.models

import com.google.gson.annotations.SerializedName

data class TodoInfoData(
    @SerializedName("title") val title: String?,
    @SerializedName("author") val author: String?,
    @SerializedName("tag") val tag: String?,
    @SerializedName("is_completed") val isCompleted: Boolean?,
    @SerializedName("priority") val priority: String?,
    @SerializedName("id") val id: Int?

)
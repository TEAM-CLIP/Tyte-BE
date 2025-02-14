package com.clip.application.todo.vo

data class TodoInfo(
    val isValid : Boolean,
    val todo : Todo
) {
    data class Todo(
        val title : String,
        val isImportant : Boolean,
        val isLife: Boolean,
        val difficulty: Int,
        val tag : String,
        val estimatedTime: Int,
        val deadline: String
    )
}
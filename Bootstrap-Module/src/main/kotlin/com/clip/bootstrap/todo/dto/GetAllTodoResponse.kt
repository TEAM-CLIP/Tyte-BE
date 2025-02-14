package com.clip.bootstrap.todo.dto

data class GetAllTodoResponse(
    val todos : List<TodoInfo>
) {
    data class TodoInfo (
        val todoId: String,
        val rawText: String,
        val title: String,
        val isImportant: Boolean,
        val isLife: Boolean,
        val difficulty: Int,
        val estimatedTime: Int,
        val deadLine: String,
        val isDone: Boolean,
        val tag: TagInfo
    )
    data class TagInfo (
        val tagId: String,
        val name: String,
        val color: String
    )
}
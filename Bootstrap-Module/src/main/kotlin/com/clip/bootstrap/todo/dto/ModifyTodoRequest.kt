package com.clip.bootstrap.todo.dto

data class ModifyTodoRequest(
    val title: String,
    val isImportant: Boolean,
    val isLife: Boolean,
    val difficulty: Int,
    val estimatedTime: Int,
    val deadLine: String,
    val tagId: String
) {
}
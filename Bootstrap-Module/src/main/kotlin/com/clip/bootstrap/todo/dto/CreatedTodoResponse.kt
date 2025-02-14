package com.clip.bootstrap.todo.dto

import java.time.LocalDate

data class CreatedTodoResponse (
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
        val deadLine: LocalDate,
        val isDone: Boolean,
        val tag: TagInfo
    )
    data class TagInfo (
        val tagId: String,
        val name: String,
        val color: String
    )
}
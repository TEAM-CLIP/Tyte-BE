package com.clip.application.todo.port.`in`

import java.time.LocalDate

interface CreateTodoUsecase {
    fun create(command: Command) : Response

    data class Command(
        val userId: String,
        val todoText : String,
        val selectedDate : LocalDate
    )

    data class Response(
        val todos : List<TodoInfo>
    ) {
        data class TodoInfo(
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
        data class TagInfo(
            val tagId: String,
            val name: String,
            val color: String
        )
    }
}
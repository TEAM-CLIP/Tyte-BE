package com.clip.application.todo.port.`in`

interface DeleteTodoUsecase {
    fun delete(command: Command)

    data class Command (
       val userId: String,
       val todoId: String
    )
}
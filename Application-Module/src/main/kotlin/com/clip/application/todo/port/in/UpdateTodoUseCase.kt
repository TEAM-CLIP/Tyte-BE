package com.clip.application.todo.port.`in`

interface UpdateTodoUseCase {
    fun update(command: Command.Update)
    fun toggleCompletion(command: Command.Complete)

    sealed class Command {
        data class Update(
            val userId: String,
            val todoId: String,
            val title: String,
            val isImportant: Boolean,
            val isLife: Boolean,
            val difficulty: Int,
            val estimatedTime: Int,
            val deadLine: String,
            val tagId: String
        ) : Command()

        data class Complete(
            val userId: String,
            val todoId: String
        ) : Command()
    }
}
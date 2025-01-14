package com.clip.application.user.port.`in`

interface DeleteUserUsecase {
    fun delete(command: Command)

    data class Command(
        val userId: String,
    )
}

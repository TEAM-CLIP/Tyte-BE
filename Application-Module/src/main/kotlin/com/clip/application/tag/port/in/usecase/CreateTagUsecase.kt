package com.clip.application.tag.port.`in`.usecase

interface CreateTagUsecase {
    fun create(command: Command)

    data class Command(
        val userId: String,
        val name: String,
        val color: String
    )
}
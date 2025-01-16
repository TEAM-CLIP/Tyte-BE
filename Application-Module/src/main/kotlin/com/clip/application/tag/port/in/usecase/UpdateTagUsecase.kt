package com.clip.application.tag.port.`in`.usecase

interface UpdateTagUsecase {
    fun update(command: Command)

    data class Command(
        val tagId: String,
        val userId: String,
        val name: String,
        val color: String
    )
}
package com.clip.application.tag.port.`in`

interface UpdateTagUsecase {
    fun update(command: Command)

    data class Command(
        val userId: String,
        val tagId: String,
        val name: String,
        val color: String
    )
}
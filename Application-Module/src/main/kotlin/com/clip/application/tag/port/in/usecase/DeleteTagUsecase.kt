package com.clip.application.tag.port.`in`.usecase

interface DeleteTagUsecase {
    fun delete(command: Command)

    data class Command(
        val tagId: String,
        val userId: String
    )
}
package com.clip.application.user.port.`in`

interface VerifyUserUsecase {

    fun verifyUser(command: Command): Response

    data class Command(
        val email: String
    )

    data class Response(
        val isDuplicated: Boolean
    )

}
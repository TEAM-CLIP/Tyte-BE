package com.clip.application.user.port.`in`

interface RegisterUserUsecase {

    fun registerSocialUser(command: Command.Social): Response
    fun registerBasicUser(command: Command.Basic): Response

    sealed class Command {
        data class Social(
            val registerToken: String,
            val nickname: String,
        ) : Command()

        class Basic(
            val email: String,
            val nickname: String,
            val password: String
        ) : Command()
    }

    data class Response(
        val accessToken: String,
        val refreshToken: String
    )
}
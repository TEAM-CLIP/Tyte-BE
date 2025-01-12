package com.clip.application.user.port.`in`


interface LoginUsecase {

    fun socialLogin(command: Command.Social): Response
    fun basicLogin(command: Command.Basic): Response

    sealed class Command {
        data class Social(
            val provider: String,
            val accessToken: String,
            val email: String? = null
        ) : Command()

        data class Basic(
            val email: String,
            val password: String
        ) : Command()
    }

    sealed class Response {
    }
    data class Success(
        val accessToken: String,
        val refreshToken: String
    ) : Response()

    data class NonRegistered(
        val registerToken: String
    ) : Response()
}
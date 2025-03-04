package com.clip.application.user.port.`in`

interface TokenResolveUsecase {

    fun resolveAccessToken(token: String): Response

    data class Response(
        val userId: String
    )
}
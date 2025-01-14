package com.clip.bootstrap.auth.controller

import com.clip.application.user.port.`in`.ReissueTokenUsecase
import com.clip.application.user.port.`in`.LoginUsecase
import com.clip.bootstrap.auth.api.AuthApi
import com.clip.bootstrap.auth.dto.*
import com.clip.common.exception.DefaultException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class AuthController(
    private val loginUsecase: LoginUsecase,
    private val reissueTokenUsecase: ReissueTokenUsecase
) : AuthApi {

    override fun socialLogin(
        request: LoginUserRequest.Social,
        provider: String
    ): ResponseEntity<LoginUserResponse.Social> {
        val command = LoginUsecase.Command.Social(
            provider = provider,
            accessToken = request.accessToken,
            email = request.email
        )
        return when (val response = loginUsecase.socialLogin(command)) {
            is LoginUsecase.Success -> ResponseEntity.ok(
                LoginUserResponse.Social.Success(
                    response.accessToken,
                    response.refreshToken
                )
            )

            is LoginUsecase.NonRegistered -> ResponseEntity.status(HttpStatus.CREATED)
                .body(LoginUserResponse.Social.NonRegistered(response.registerToken))
        }
    }

    override fun login(request: LoginUserRequest.Basic): ResponseEntity<LoginUserResponse.Basic> {
        val command = LoginUsecase.Command.Basic(
            email = request.email,
            password = request.password
        )
        return when (val response = loginUsecase.basicLogin(command)) {
            is LoginUsecase.Success -> ResponseEntity.ok(
                LoginUserResponse.Basic(
                    accessToken = response.accessToken,
                    refreshToken = response.refreshToken
                )
            )
            else -> throw DefaultException.InvalidStateException("잘못된 일반 로그인 응답입니다.")
        }
    }

    override fun reissueToken(request: ReissueRequest): ReissueResponse {
        val response = reissueTokenUsecase.reissue(
            ReissueTokenUsecase.Command(
                refreshToken = request.refreshToken
            )
        )
        return ReissueResponse(
            accessToken = response.accessToken,
            refreshToken = response.refreshToken
        )
    }

}
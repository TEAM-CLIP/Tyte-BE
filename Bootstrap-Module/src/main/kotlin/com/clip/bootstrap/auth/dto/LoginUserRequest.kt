package com.clip.bootstrap.auth.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "로그인 요청",
    oneOf = [
        LoginUserRequest.Social::class,
        LoginUserRequest.Basic::class
    ]
)
sealed class LoginUserRequest {

    @Schema(description = "소셜 로그인 요청")
    data class Social(
        @Schema(description = "oauth access token")
        val accessToken: String,

        @Schema(description = "사용자 이메일 (애플 로그인 시 필수)")
        val email: String?
    ) : LoginUserRequest()

    @Schema(description = "일반 로그인 요청")
    data class Basic(
        @Schema(description = "사용자 이메일")
        val email: String,

        @Schema(description = "사용자 비밀번호")
        val password: String
    ) : LoginUserRequest()
}
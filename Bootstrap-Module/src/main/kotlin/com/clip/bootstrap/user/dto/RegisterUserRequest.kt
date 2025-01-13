package com.clip.bootstrap.user.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(
    description = "회원 가입 요청",
    oneOf = [
        RegisterUserRequest.Social::class,
        RegisterUserRequest.Basic::class
    ]
)
sealed class RegisterUserRequest {

    @Schema(description = "소셜 로그인을 통한 회원 가입")
    data class Social(
        @Schema(description = "register_token, 소셜 로그인이로부터 전달받은 토큰")
        val registerToken: String,
        @Schema(description = "닉네임")
        val nickname: String,
    ) : RegisterUserRequest()

    @Schema(description = "일반 회원 가입")
    data class Basic(
        @Schema(description = "이메일")
        val email: String,
        @Schema(description = "닉네임")
        val nickname: String,
        @Schema(description = "비밀번호")
        val password: String
    ) : RegisterUserRequest()
}
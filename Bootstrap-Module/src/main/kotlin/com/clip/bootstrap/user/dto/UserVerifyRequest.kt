package com.clip.bootstrap.user.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "닉네임 중복 확인 요청")
data class UserVerifyRequest(
    @Schema(description = "이메일")
    val email: String
)

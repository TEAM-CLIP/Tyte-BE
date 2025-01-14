package com.clip.bootstrap.user.dto

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "계정 존재 유무 확인 응답")
data class UserVerifyResponse(
    @Schema(description = "계정 존재 유무 여부")
    val isDuplicated: Boolean
) {
}

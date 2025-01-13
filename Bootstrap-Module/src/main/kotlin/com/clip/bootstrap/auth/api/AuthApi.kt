package com.clip.bootstrap.auth.api

import com.clip.bootstrap.auth.dto.*
import com.clip.bootstrap.common.exception.ExceptionResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@Tag(name = "Auth", description = "Auth API")
@RequestMapping("/api/v1/auth")
interface AuthApi {

    @Operation(summary = "소셜 로그인")
    @PostMapping("/social-login/{provider}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "기존 회원 로그인 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LoginUserResponse.Social.Success::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "201",
                description = "신규 회원 가입 필요",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LoginUserResponse.Social.NonRegistered::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "4XX",
                description = "소셜 로그인 실패",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ExceptionResponse::class)
                    )
                ]
            )
        ]
    )
    fun socialLogin(
        @RequestBody request: LoginUserRequest.Social, @PathVariable provider: String
    ): ResponseEntity<LoginUserResponse.Social>

    @Operation(summary = "일반 로그인")
    @PostMapping("/login")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "기존 회원 로그인 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = LoginUserResponse.Basic::class)
                    )
                ]
            )
        ]
    )
    fun login(
        @RequestBody request: LoginUserRequest.Basic
    ): ResponseEntity<LoginUserResponse.Basic>


    @Operation(summary = "토큰 재발급")
    @PostMapping("/reissue")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "토큰 재발급 성공",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ReissueResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "4XX",
                description = "토큰 재발급 실패, 다시 로그인 필요",
                content = [
                    Content(
                        mediaType = "application/json",
                        schema = Schema(implementation = ExceptionResponse::class)
                    )
                ]
            ),
        ]
    )
    fun reissueToken(
        @RequestBody request: ReissueRequest
    ): ReissueResponse
}
package com.clip.bootstrap.controller.auth

import com.clip.application.user.port.`in`.LoginUsecase
import com.clip.application.user.port.`in`.LogoutUsecase
import com.clip.application.user.port.`in`.ReissueTokenUsecase
import com.clip.application.user.port.`in`.TokenResolveUsecase
import com.clip.bootstrap.ControllerSupporter
import com.clip.bootstrap.auth.dto.LoginUserRequest
import com.clip.bootstrap.auth.dto.ReissueRequest
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post

class AuthControllerTest : ControllerSupporter() {
    @MockBean
    private lateinit var loginUsecase: LoginUsecase

    @MockBean
    private lateinit var reissueTokenUsecase: ReissueTokenUsecase

    @MockBean
    private lateinit var tokenResolveUsecase: TokenResolveUsecase

    @MockBean
    private lateinit var logoutUsecase: LogoutUsecase

    @Test
    fun socialLoginSuccessTest() {
        // given
        val request = LoginUserRequest.Social("registered", "email")
        val command = LoginUsecase.Command.Social("GOOGLE", "registered", "email")
        given(loginUsecase.socialLogin(command))
            .willReturn(LoginUsecase.Success("accessToken", "refreshToken"))
        // when
        val response =
            mockMvc.post("/api/v1/auth/social-login/{provider}", "GOOGLE") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

        // then
        response.andExpect {
            status { isOk() }
            jsonPath("$.accessToken") {
                exists()
                isString()
                isNotEmpty()
            }
            jsonPath("$.refreshToken") {
                exists()
                isString()
                isNotEmpty()
            }
        }
    }

    @Test
    fun socialLoginNonRegisteredTest() {
        // given
        val request = LoginUserRequest.Social("nonRegistered", "email")
        val command = LoginUsecase.Command.Social("GOOGLE", "nonRegistered", "email")
        given(loginUsecase.socialLogin(command))
            .willReturn(LoginUsecase.NonRegistered("registerToken"))
        // when
        val response =
            mockMvc.post("/api/v1/auth/social-login/{provider}", "GOOGLE") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

        // then
        response.andExpect {
            status { is2xxSuccessful() }
            jsonPath("$.registerToken") {
                exists()
                isString()
                isNotEmpty()
            }
        }
    }

    @Test
    fun basicLoginTest() {
        // given
        val request = LoginUserRequest.Basic("email", "password")
        val command = LoginUsecase.Command.Basic("email", "password")
        given(loginUsecase.basicLogin(command))
            .willReturn(LoginUsecase.Success("accessToken", "refreshToken"))
        // when
        val response =
            mockMvc.post("/api/v1/auth/login") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

        // then
        response.andExpect {
            status { isOk() }
            jsonPath("$.accessToken") {
                exists()
                isString()
                isNotEmpty()
            }
            jsonPath("$.refreshToken") {
                exists()
                isString()
                isNotEmpty()
            }
        }
    }

    @Test
    fun reissueTokenTest() {
        // given
        val request = ReissueRequest("refreshToken")
        BDDMockito
            .given(reissueTokenUsecase.reissue(ReissueTokenUsecase.Command(request.refreshToken)))
            .willReturn(ReissueTokenUsecase.Response("accessToken", "refreshToken"))
        // when
        val response =
            mockMvc.post("/api/v1/auth/reissue") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }

        // then
        response.andExpect {
            status { isOk() }
            jsonPath("$.accessToken") {
                exists()
                isString()
                isNotEmpty()
            }
            jsonPath("$.refreshToken") {
                exists()
                isString()
                isNotEmpty()
            }
        }
    }
}
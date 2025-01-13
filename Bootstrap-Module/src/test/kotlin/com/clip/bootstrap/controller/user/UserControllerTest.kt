package com.clip.bootstrap.controller.user


import com.clip.application.user.port.`in`.*
import com.clip.bootstrap.ControllerSupporter
import com.clip.bootstrap.user.dto.LogoutRequest
import com.clip.bootstrap.user.dto.RegisterUserRequest
import org.junit.jupiter.api.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

class UserControllerTest : ControllerSupporter() {
    @MockBean
    private lateinit var registerUserUsecase: RegisterUserUsecase

    @MockBean
    private lateinit var logoutUsecase: LogoutUsecase

    @MockBean
    private lateinit var userVerifyUserUsecase: VerifyUserUsecase

    @MockBean
    private lateinit var tokenResolveUsecase: TokenResolveUsecase

    @MockBean
    private lateinit var deleteUserUsecase: DeleteUserUsecase

    @MockBean
    private lateinit var reissueTokenUsecase: ReissueTokenUsecase


    @Test
    fun registerBasicUserTest() {
        // given
        val request = RegisterUserRequest.Basic("email", "nickname", "password")
        val command =
            RegisterUserUsecase.Command.Basic(
                request.email,
                request.nickname,
                request.password,
            )
        given(registerUserUsecase.registerBasicUser(command)).willReturn(
            RegisterUserUsecase.Response(
                "accessToken",
                "refreshToken"
            )
        )
        // when
        val response =
            mockMvc.post("/api/v1/users/register") {
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
    fun registerSocialUserTest() {
        // given
        val request = RegisterUserRequest.Social("register",  "nickname")
        val command =
            RegisterUserUsecase.Command.Social(
                request.registerToken,
                request.nickname,
            )
        given(registerUserUsecase.registerSocialUser(command)).willReturn(
            RegisterUserUsecase.Response(
                "accessToken",
                "refreshToken"
            )
        )
        // when
        val response =
            mockMvc.post("/api/v1/users/social-register") {
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
    fun logout() {
        // given
        val userId = userMockManager.settingUser()
        val refreshToken = jwtMockManager.generateRefreshToken(userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val request = LogoutRequest(refreshToken)
        given(tokenResolveUsecase.resolveAccessToken(accessToken)).willReturn(TokenResolveUsecase.Response(userId))
        // when
        val response =
            mockMvc.delete("/api/v1/users/logout") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
                header("Authorization", "Bearer $accessToken")
            }

        // then
        response.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun deleteUser() {
        // given
        val userId = userMockManager.settingUser()
        val accessToken = jwtMockManager.generateAccessToken(userId)
        given(tokenResolveUsecase.resolveAccessToken(accessToken)).willReturn(TokenResolveUsecase.Response(userId))

        // when
        val response =
            mockMvc.delete("/api/v1/users") {
                contentType = MediaType.APPLICATION_JSON
                header("Authorization", "Bearer $accessToken")
            }

        // then
        response.andExpect {
            status { isOk() }
        }
    }

}

package com.clip.bootstrap.integration.user


import com.asap.bootstrap.IntegrationSupporter
import com.clip.application.user.exception.UserException
import com.clip.bootstrap.user.dto.LogoutRequest
import com.clip.bootstrap.user.dto.RegisterUserRequest
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post

class UserApiIntegrationTest : IntegrationSupporter() {
    @Test
    fun registerUserSuccessTest() {
        // given
        val registerToken = jwtMockManager.generateRegisterToken()
        val request = RegisterUserRequest.Social(registerToken, "nickname")
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
    fun registerUserInvalidTest_with_DuplicateUser() {
        // given
        val duplicateRegisterToken = jwtMockManager.generateRegisterToken()
        val request = RegisterUserRequest.Social(duplicateRegisterToken, "nickname")
        mockMvc.post("/api/v1/users/social-register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
        }
        // when
        val response =
            mockMvc.post("/api/v1/users/social-register") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
        // then
        response.andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    fun registerUserInvalidTest_with_InvalidRegisterToken() {
        // given
        val registerToken = jwtMockManager.generateInvalidToken()
        val request = RegisterUserRequest.Social(registerToken, "nickname")
        // when
        val response =
            mockMvc.post("/api/v1/users/social-register") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
        // then
        response.andExpect {
            status { isUnauthorized() }
            jsonPath("$.code") {
                exists()
                isString()
                value(UserException.UserPermissionDeniedException().code)
            }
        }
    }


    @Nested
    inner class LogoutTest {
        @Test
        fun logout() {
            // given
            val userId = userMockManager.settingUser()
            val refreshToken = jwtMockManager.generateRefreshToken(userId)
            val accessToken = jwtMockManager.generateAccessToken(userId)
            val request = LogoutRequest(refreshToken)
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
        fun logout_with_InvalidToken() {
            // given
            val userId = userMockManager.settingUser()
            val refreshToken = jwtMockManager.generateInvalidToken()
            val accessToken = jwtMockManager.generateAccessToken(userId)
            val request = LogoutRequest(refreshToken)
            // when
            val response =
                mockMvc.delete("/api/v1/users/logout") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                    header("Authorization", "Bearer $accessToken")
                }
            // then
            response.andExpect {
                status { isUnauthorized() }
            }
        }
    }

    @Test
    fun deleteUser() {
        // given
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)

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

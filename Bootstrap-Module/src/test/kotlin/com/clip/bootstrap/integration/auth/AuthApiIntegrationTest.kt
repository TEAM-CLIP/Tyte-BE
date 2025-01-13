package com.clip.bootstrap.integration.auth

import com.asap.bootstrap.IntegrationSupporter
import com.clip.bootstrap.auth.dto.LoginUserRequest
import com.clip.bootstrap.auth.dto.ReissueRequest
import com.clip.client.GoogleTestData
import com.clip.client.MockServer
import com.clip.security.jwt.user.TokenType
import org.junit.jupiter.api.Nested
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.post
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*
import kotlin.test.Test

class AuthApiIntegrationTest : IntegrationSupporter() {
    @Autowired
    lateinit var mockWebServer: MockServer

    @Test
    fun socialLoginSuccessTest() {
        // given
        val request = LoginUserRequest.Social("registered", "email")
        val provider = "GOOGLE"
        mockWebServer.enqueue(GoogleTestData.OAUTH_SUCCESS_RESPONSE)
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId, "socialId", provider)
        // when
        val response =
            mockMvc.post("/api/v1/auth/social-login/{provider}", provider) {
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
        val provider = "GOOGLE"
        mockWebServer.enqueue(GoogleTestData.OAUTH_FAIL_RESPONSE_WITH_NON_REGISTERED)
        // when
        val response =
            mockMvc.post("/api/v1/auth/social-login/{provider}", provider) {
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
    fun socialLoginBadRequestTest_with_invalid_access_token() {
        // given
        val request = LoginUserRequest.Social("invalid", "email")
        val provider = "GOOGLE"
        mockWebServer.enqueue(GoogleTestData.OAUTH_FAIL_RESPONSE_WITH_INVALID_ACCESS_TOKEN)
        // when
        val response =
            mockMvc.post("/api/v1/auth/social-login/{provider}", provider) {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
            }
        // then
        response.andExpect {
            status { isBadRequest() }
        }
    }

    @Nested
    inner class ReissueTest {
        @Test
        fun reissueTokenTest() {
            // given
            val userId = userMockManager.settingUser()
            val refreshToken =
                jwtMockManager.generateRefreshToken(
                    userId,
                    issuedAt =
                    Date(
                        LocalDateTime
                            .now()
                            .minusHours(1)
                            .toInstant(
                                ZoneOffset.UTC,
                            ).toEpochMilli(),
                    ),
                )
            val request = ReissueRequest(refreshToken)
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

        @Test
        fun reissueTokenTest_With_Expired_Token() {
            // given
            val userId = userMockManager.settingUser()
            val refreshToken = jwtMockManager.generateExpiredToken(TokenType.REFRESH, userId)
            val request = ReissueRequest(refreshToken)
            // when
            val response =
                mockMvc.post("/api/v1/auth/reissue") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
            // then
            response.andExpect {
                status { isUnauthorized() }
            }
        }

        @Test
        fun reissueTokenTest_With_Non_Saved_Token() {
            // given
            val request = ReissueRequest("invalidToken")
            // when
            val response =
                mockMvc.post("/api/v1/auth/reissue") {
                    contentType = MediaType.APPLICATION_JSON
                    content = objectMapper.writeValueAsString(request)
                }
            // then
            response
                .andExpect {
                    status { isUnauthorized() }
                }.andDo {
                    print()
                }
        }
    }
}

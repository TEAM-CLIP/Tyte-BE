package com.clip.security.jwt


import com.clip.application.user.port.out.UserTokenManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.UserToken
import com.clip.domain.user.enums.LoginProvider
import com.clip.security.jwt.user.TokenType
import com.clip.security.jwt.user.UserJwtClaims
import com.clip.security.jwt.user.UserJwtProperties
import com.clip.security.jwt.user.UserRegisterJwtClaims
import java.util.*

class JwtMockManager(
    private val userJwtProperties: UserJwtProperties,
    private val userTokenManagementPort: UserTokenManagementPort,
) {
    fun generateRegisterToken(
        socialId: String = DomainId.generate().value,
        loginProvider: String = LoginProvider.GOOGLE.name,
        issuedAt: Date = Date(),
    ): String =
        JwtProvider
            .createToken(
                JwtPayload(
                    issuedAt = issuedAt,
                    issuer = UserJwtProperties.ISSUER,
                    subject = UserJwtProperties.SUBJECT,
                    expireTime = UserJwtProperties.REGISTER_TOKEN_EXPIRE_TIME,
                    claims =
                        UserRegisterJwtClaims(
                            socialId = socialId,
                            loginProvider = LoginProvider.parse(loginProvider),
                            email = "email",
                        ),
                ),
                userJwtProperties.secret,
            ).apply {
                userTokenManagementPort.saveUserToken(UserToken(token = this))
            }

    fun generateAccessToken(
        userId: String = DomainId.generate().value,
        issuedAt: Date = Date(),
    ): String =
        JwtProvider.createToken(
            JwtPayload(
                issuedAt = issuedAt,
                issuer = UserJwtProperties.ISSUER,
                subject = UserJwtProperties.SUBJECT,
                expireTime = UserJwtProperties.ACCESS_TOKEN_EXPIRE_TIME,
                claims =
                    UserJwtClaims(
                        userId = userId,
                        tokenType = TokenType.ACCESS,
                    ),
            ),
            userJwtProperties.secret,
        )

    fun generateRefreshToken(
        userId: String = "userId",
        issuedAt: Date = Date(),
    ): String =
        JwtProvider
            .createToken(
                JwtPayload(
                    issuedAt = issuedAt,
                    issuer = UserJwtProperties.ISSUER,
                    subject = UserJwtProperties.SUBJECT,
                    expireTime = UserJwtProperties.REFRESH_TOKEN_EXPIRE_TIME,
                    claims =
                        UserJwtClaims(
                            userId = userId,
                            tokenType = TokenType.REFRESH,
                        ),
                ),
                userJwtProperties.secret,
            ).apply {
                userTokenManagementPort.saveUserToken(UserToken(token = this, userId = DomainId(userId)))
            }

    fun generateExpiredToken(
        tokenType: TokenType,
        userId: String = "userId",
    ): String =
        JwtProvider
            .createToken(
                JwtPayload(
                    issuedAt = Date(System.currentTimeMillis() - tokenTypeExpireTime(tokenType)),
                    issuer = UserJwtProperties.ISSUER,
                    subject = UserJwtProperties.SUBJECT,
                    expireTime = 1,
                    claims =
                        UserJwtClaims(
                            userId = userId,
                            tokenType = tokenType,
                        ),
                ),
                userJwtProperties.secret,
            ).also {
                when (tokenType) {
                    TokenType.REFRESH ->
                        userTokenManagementPort.saveUserToken(
                            UserToken(
                                token = it,
                                userId = DomainId(userId),
                            ),
                        )

                    else -> {}
                }
            }

    private fun tokenTypeExpireTime(tokenType: TokenType): Long =
        when (tokenType) {
            TokenType.ACCESS -> UserJwtProperties.ACCESS_TOKEN_EXPIRE_TIME
            TokenType.REFRESH -> UserJwtProperties.REFRESH_TOKEN_EXPIRE_TIME
        }

    fun generateInvalidToken(): String = "invalidToken"
}

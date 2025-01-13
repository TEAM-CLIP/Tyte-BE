package com.clip.client.oauth.platform

import com.clip.client.oauth.OAuthRetrieveHandler
import com.clip.client.oauth.exception.OAuthException
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient

@Component
class GoogleAuthRetrieveHandler(
    @Qualifier("googleWebClient") private val googleWebClient: WebClient,
) : OAuthRetrieveHandler {
    override fun getOAuthInfo(request: OAuthRetrieveHandler.OAuthRequest): OAuthRetrieveHandler.OAuthResponse =
        googleWebClient
            .get()
            .uri("/oauth2/v3/userinfo")
            .header("Authorization", "Bearer ${request.accessToken}")
            .retrieve()
            .onStatus({ it.isError }, {
                throw OAuthException.OAuthRetrieveFailedException("구글 사용자 정보를 가져오는 중 HTTP 에러가 발생했습니다: ${it.statusCode()}")
            })
            .bodyToMono(UserInfo::class.java)
            .block()
            ?.let {
                OAuthRetrieveHandler.OAuthResponse(
                    socialId = it.sub,
                    email = it.email
                )
            } ?: throw OAuthException.OAuthRetrieveFailedException("구글 사용자 정보를 가져오는데 실패했습니다.")

    data class UserInfo(
        val sub: String,
        val email: String
    )

}

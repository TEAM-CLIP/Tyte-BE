package com.clip.client.oauth

import com.clip.client.oauth.platform.AppleOAuthRetrieveHandler
import com.clip.client.oauth.platform.GoogleAuthRetrieveHandler
import com.clip.domain.user.enums.LoginProvider
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OAuthConfig(
    private val googleAuthRetrieveHandler: GoogleAuthRetrieveHandler,
    private val appleOAuthRetrieveHandler: AppleOAuthRetrieveHandler,
) {
    @Bean
    @Qualifier("oAuthRetrieveHandlers")
    fun oAuthRetrieveHandlers(): Map<LoginProvider, OAuthRetrieveHandler> =
        mapOf(
            LoginProvider.GOOGLE to googleAuthRetrieveHandler as OAuthRetrieveHandler,
            LoginProvider.APPLE to appleOAuthRetrieveHandler as OAuthRetrieveHandler,
        )
}

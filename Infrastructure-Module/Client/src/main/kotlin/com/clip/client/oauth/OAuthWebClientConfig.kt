package com.clip.client.oauth

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class OAuthWebClientConfig {

    @Bean
    @Qualifier("googleWebClient")
    fun googleWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://www.googleapis.com")
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build()
    }

    @Bean
    @Qualifier("appleWebClient")
    fun appleWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://appleid.apple.com")
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build()
    }


}
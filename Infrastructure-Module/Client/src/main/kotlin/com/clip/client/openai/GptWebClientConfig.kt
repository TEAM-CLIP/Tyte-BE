package com.clip.client.openai

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
@EnableConfigurationProperties(OpenAIProperties::class)
class GptWebClientConfig {

    @Bean
    @Qualifier("gptWebClient")
    fun gptWebClient(): WebClient {
        return WebClient.builder()
            .baseUrl("https://api.openai.com")
            .defaultHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}
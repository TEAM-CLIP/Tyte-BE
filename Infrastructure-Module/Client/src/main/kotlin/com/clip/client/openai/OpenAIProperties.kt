package com.clip.client.openai

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "openai")
data class OpenAIProperties(
    val key: String,
    val prompt: String
)
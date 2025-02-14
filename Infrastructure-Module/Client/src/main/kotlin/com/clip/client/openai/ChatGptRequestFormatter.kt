package com.clip.client.openai

object ChatGptRequestFormatter {
    fun formatRequest(prompt: String, requestMessage: String) = mapOf(
            "model" to "gpt-4o",
            "messages" to listOf(
                mapOf(
                    "role" to "system",
                    "content" to prompt
                ),
                mapOf(
                    "role" to "user",
                    "content" to requestMessage
                )
            )
        )
}
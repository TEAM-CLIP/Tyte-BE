package com.clip.client.openai

import com.clip.application.todo.vo.TodoInfo
import com.clip.client.openai.exception.OpenAIException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers

/*
 * @Todo : 최적화 필요 (2025-02-15)
 */
@Component
class GptCreateTodoClient(
    @Qualifier("gptWebClient") private val gptWebClient: WebClient,
    private val openAIProperties: OpenAIProperties,
    private val objectMapper: ObjectMapper
) {
    private val logger = KotlinLogging.logger {}

    fun createTodos(todoTexts: List<String>, tagNames: List<String>): List<TodoInfo> {
        return Flux.fromIterable(todoTexts)
            .parallel() // 병렬 처리 활성화
            .runOn(Schedulers.boundedElastic()) // I/O 작업 스케줄러
            .flatMap { todoText ->
                createGptRequestAsync(todoText, tagNames)
                    .doOnError { error ->
                        logger.error { "[API 호출 실패] 입력: '$todoText', 원인: ${error.message}" }
                    }
                    .onErrorResume { Mono.empty() } // 실패 시 스트림에서 제외 (부분 성공 허용)
            }
            .sequential() // 순차 처리로 변환
            .filter { it.isValid } // 유효한 응답만 필터링
            .collectList()
            .block() ?: emptyList()
    }

    private fun createGptRequestAsync(
        todoText: String,
        tagNames: List<String>
    ): Mono<TodoInfo> {
        return gptWebClient.post()
            .uri("/v1/chat/completions")
            .bodyValue(ChatGptRequestFormatter.formatRequest(openAIProperties.prompt, createInputMessage(todoText, tagNames)))
            .header("Authorization", "Bearer ${openAIProperties.key}")
            .retrieve()
            .bodyToMono(OpenAiResponse::class.java)
            .flatMap { openAiResponse ->
                processOpenAiResponse(openAiResponse, todoText)
            }
    }

    private fun processOpenAiResponse(
        response: OpenAiResponse,
        todoText: String
    ): Mono<TodoInfo> {
        println("response: ${response.choices.firstOrNull()?.message?.content}")
        return Mono.justOrEmpty(response.choices.firstOrNull()?.message?.content)
            .filter { it.isNotBlank() }
            .switchIfEmpty(
                Mono.defer {
                    logger.error { "[컨텐츠 없음] 입력: $todoText" }
                    Mono.empty()
                }
            )
            .flatMap { content ->
                Mono.fromCallable {
                    objectMapper.readValue<TodoInfo>(content)
                }
                    .onErrorResume { e ->
                        logger.error(e) { "[파싱 실패] 입력: $todoText" }
                        Mono.empty()
                    }
            }
            .filter { it.isValid }
            .switchIfEmpty(
                Mono.defer {
                    logger.error { "[유효성 검증 실패] 입력: $todoText" }
                    Mono.error(OpenAIException.OpenAIRetrieveFailedException())
                }
            )
    }



    private fun createInputMessage(
        todoText: String,
        tagNames: List<String>
    ): String {
        return """
            내용: $todoText
            태그 배열: ${tagNames.joinToString()}
        """.trimIndent()
    }

    data class OpenAiResponse(
        val choices: List<Choice>
    ) {
        data class Choice(
            val message: Message
        )
        data class Message(
            val content: String?
        )
    }

}
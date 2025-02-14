package com.clip.client.openai

import com.clip.application.todo.port.out.TodoInfoRetrievePort
import com.clip.application.todo.vo.TodoInfo
import org.springframework.stereotype.Component

@Component
class TodoInfoRetrieveAdapter(
    private val gptCreateTodoClient: GptCreateTodoClient
): TodoInfoRetrievePort {
    override fun getTodoInfo(todoTexts: List<String>, tagNames: List<String>): List<TodoInfo> {
        return gptCreateTodoClient.createTodos(todoTexts, tagNames)
    }
}
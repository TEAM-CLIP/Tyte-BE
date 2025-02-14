package com.clip.application.todo.service

import com.clip.application.tag.port.out.TagManagementPort
import com.clip.application.todo.port.`in`.GetTodoUsecase
import com.clip.application.todo.port.out.TodoManagementPort
import com.clip.domain.common.DomainId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.format.DateTimeFormatter

@Service
@Transactional
class TodoQueryService(
    private val todoManagementPort: TodoManagementPort,
    private val tagManagementPort: TagManagementPort
) : GetTodoUsecase {
    override fun getAll(command: GetTodoUsecase.Command): GetTodoUsecase.Response {
        val todos =
            todoManagementPort.getAllTodoBy(userId = DomainId(command.userId), selectedDate = command.selectedDate)
        val tagIds = todos.mapNotNull { it.tagId }
        val tagMap = tagManagementPort.getAllTagByIds(tagIds).associateBy { it.id }
        return GetTodoUsecase.Response(
            todos = todos.map { todo ->
                GetTodoUsecase.Response.TodoInfo(
                    todoId = todo.id.value,
                    rawText = todo.raw,
                    title = todo.title,
                    isImportant = todo.isImportant,
                    isLife = todo.isLife,
                    difficulty = todo.difficulty,
                    estimatedTime = todo.estimatedTime,
                    deadLine = todo.deadline.format(DateTimeFormatter.ISO_LOCAL_DATE),
                    isDone = todo.isCompleted,
                    tag = todo.tagId?.let { tagMap[it] }?.let {
                        GetTodoUsecase.Response.TagInfo(
                            tagId = it.id.value,
                            name = it.name,
                            color = it.color
                        )
                    } ?: GetTodoUsecase.Response.TagInfo(
                        tagId = "",
                        name = "",
                        color = ""
                    )
                )
            }
        )
    }
}

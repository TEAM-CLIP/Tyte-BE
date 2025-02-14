package com.clip.application.todo.service

import com.clip.application.tag.port.out.TagManagementPort
import com.clip.application.todo.port.`in`.CreateTodoUsecase
import com.clip.application.todo.port.`in`.DeleteTodoUsecase
import com.clip.application.todo.port.`in`.UpdateTodoUseCase
import com.clip.application.todo.port.out.TodoInfoRetrievePort
import com.clip.application.todo.port.out.TodoManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.todo.entity.Todo
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Service
@Transactional
class TodoCommandService(
    private val todoManagementPort: TodoManagementPort,
    private val todoInfoRetrievePort: TodoInfoRetrievePort,
    private val tagManagementPort: TagManagementPort
): CreateTodoUsecase, UpdateTodoUseCase, DeleteTodoUsecase {
    override fun create(command: CreateTodoUsecase.Command): CreateTodoUsecase.Response {
        val todoTexts = command.todoText.splitToSequence(",")
            .map { it.trim() }
            .filter { it.isNotEmpty() }
            .toList()

        val (tagNameMap, tagIdMap) = tagManagementPort.getAllTagBy(DomainId(command.userId))
            .let { tags ->
                Pair(
                    tags.associateBy { it.name },
                    tags.associateBy { it.id }
                )
            }

        val todoInfos = todoInfoRetrievePort.getTodoInfo(
            todoTexts = todoTexts,
            tagNames = tagNameMap.keys.toList()
        )

        val todosToSave = todoInfos.map { info ->
            Todo.create(
                userId = DomainId(command.userId),
                tagId = info.todo.tag.let { tagNameMap[it]?.id },
                raw = command.todoText,
                title = info.todo.title,
                isImportant = info.todo.isImportant,
                isLife = info.todo.isLife,
                difficulty = info.todo.difficulty,
                estimatedTime = info.todo.estimatedTime,
                deadline = LocalDate.parse(info.todo.deadline, DateTimeFormatter.ISO_LOCAL_DATE),
                isCompleted = false
            )
        }

        val savedTodos = todoManagementPort.saveTodos(todosToSave)

        return CreateTodoUsecase.Response(
            todos = savedTodos.map { todo ->
                val tag = todo.tagId?.let { tagIdMap[it] }

                CreateTodoUsecase.Response.TodoInfo(
                    todoId = todo.id.value,
                    rawText = todo.raw,
                    title = todo.title,
                    isImportant = todo.isImportant,
                    isLife = todo.isLife,
                    difficulty = todo.difficulty,
                    estimatedTime = todo.estimatedTime,
                    deadLine = todo.deadline,
                    isDone = todo.isCompleted,
                    tag = tag?.let {
                        CreateTodoUsecase.Response.TagInfo(
                            tagId = it.id.value,
                            name = it.name,
                            color = it.color
                        )
                    } ?: CreateTodoUsecase.Response.TagInfo(
                        tagId = "",
                        name = "",
                        color = ""
                    )
                )
            }
        )
    }

    override fun update(command: UpdateTodoUseCase.Command.Update) {
        val todo = todoManagementPort.getTodoNotNull(
            todoId = DomainId(command.todoId),
            userId = DomainId(command.userId)
        )

        todo.update(
            title = command.title,
            isImportant = command.isImportant,
            isLife = command.isLife,
            difficulty = command.difficulty,
            estimatedTime = command.estimatedTime,
            deadLine = command.deadLine,
            tagId = command.tagId
        )
    }

    override fun toggleCompletion(command: UpdateTodoUseCase.Command.Complete) {
        val todo = todoManagementPort.getTodoNotNull(
            todoId = DomainId(command.todoId),
            userId = DomainId(command.userId)
        )

        todo.toggleCompletion()

        todoManagementPort.updateTodo(todo)
    }

    override fun delete(command: DeleteTodoUsecase.Command) {
        val todo = todoManagementPort.getTodoNotNull(
            todoId = DomainId(command.todoId),
            userId = DomainId(command.userId)
        )

        todoManagementPort.deleteTodo(todo)
    }


}
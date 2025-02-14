package com.clip.bootstrap.todo.controller

import com.clip.application.todo.port.`in`.CreateTodoUsecase
import com.clip.application.todo.port.`in`.DeleteTodoUsecase
import com.clip.application.todo.port.`in`.GetTodoUsecase
import com.clip.application.todo.port.`in`.UpdateTodoUseCase
import com.clip.bootstrap.todo.api.TodoApi
import com.clip.bootstrap.todo.dto.CreateTodoRequest
import com.clip.bootstrap.todo.dto.CreatedTodoResponse
import com.clip.bootstrap.todo.dto.GetAllTodoResponse
import com.clip.bootstrap.todo.dto.ModifyTodoRequest
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
class TodoController(
    private val createTodoUsecase: CreateTodoUsecase,
    private val getTodoUsecase: GetTodoUsecase,
    private val deleteTodoUsecase: DeleteTodoUsecase,
    private val updateTodoUsecase: UpdateTodoUseCase
) : TodoApi {
    override fun createTodo(userId: String, request: CreateTodoRequest): CreatedTodoResponse =
        createTodoUsecase.create(
            CreateTodoUsecase.Command(
                userId = userId,
                todoText = request.todoText,
                selectedDate = request.selectedDate
            )
        ).let { response ->
            CreatedTodoResponse(
                todos = response.todos.map { todo ->
                    CreatedTodoResponse.TodoInfo(
                        todoId = todo.todoId,
                        rawText = todo.rawText,
                        title = todo.title,
                        isImportant = todo.isImportant,
                        isLife = todo.isLife,
                        difficulty = todo.difficulty,
                        estimatedTime = todo.estimatedTime,
                        deadLine = todo.deadLine,
                        isDone = todo.isDone,
                        tag = CreatedTodoResponse.TagInfo(
                            tagId = todo.tag.tagId,
                            name = todo.tag.name,
                            color = todo.tag.color
                        )
                    )
                }
            )
        }

    override fun getTodos(userId: String, deadline: LocalDate): GetAllTodoResponse {
        val response = getTodoUsecase.getAll(
            GetTodoUsecase.Command(
                userId = userId,
                selectedDate = deadline
            )
        )

        return GetAllTodoResponse(
            todos = response.todos.map { todo ->
                GetAllTodoResponse.TodoInfo(
                    todoId = todo.todoId,
                    rawText = todo.rawText,
                    title = todo.title,
                    isImportant = todo.isImportant,
                    isLife = todo.isLife,
                    difficulty = todo.difficulty,
                    estimatedTime = todo.estimatedTime,
                    deadLine = todo.deadLine,
                    isDone = todo.isDone,
                    tag = GetAllTodoResponse.TagInfo(
                        tagId = todo.tag.tagId,
                        name = todo.tag.name,
                        color = todo.tag.color
                    )
                )
            }
        )
    }

    override fun deleteTodo(userId: String, todoId: String) {
        deleteTodoUsecase.delete(
            DeleteTodoUsecase.Command(
                userId = userId,
                todoId = todoId
            )
        )
    }

    override fun updateTodo(userId: String, todoId: String, request: ModifyTodoRequest) {
        updateTodoUsecase.update(
            UpdateTodoUseCase.Command.Update(
                userId = userId,
                todoId = todoId,
                title = request.title,
                isImportant = request.isImportant,
                isLife = request.isLife,
                difficulty = request.difficulty,
                estimatedTime = request.estimatedTime,
                deadLine = request.deadLine,
                tagId = request.tagId
            )
        )

    }

    override fun completeTodo(userId: String, todoId: String) {
        updateTodoUsecase.toggleCompletion(
            UpdateTodoUseCase.Command.Complete(
                userId = userId,
                todoId = todoId
            )
        )
    }


}
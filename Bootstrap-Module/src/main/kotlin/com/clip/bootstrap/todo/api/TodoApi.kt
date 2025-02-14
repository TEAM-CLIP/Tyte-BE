package com.clip.bootstrap.todo.api

import com.clip.bootstrap.common.security.annotation.AccessUser
import com.clip.bootstrap.todo.dto.CreateTodoRequest
import com.clip.bootstrap.todo.dto.CreatedTodoResponse
import com.clip.bootstrap.todo.dto.GetAllTodoResponse
import com.clip.bootstrap.todo.dto.ModifyTodoRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@Tag(name = "Todo", description = "Todo API")
@RequestMapping("/api/v1/todos")
interface TodoApi {

    @Operation(summary = "투두 생성")
    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "투두 생성 성공",
                content = [
                    Content(
                        schema = Schema(implementation = CreatedTodoResponse::class),
                    ),
                ],
            ),
        ],
    )
    fun createTodo(
        @AccessUser userId: String,
        @RequestBody request: CreateTodoRequest,
    ) : CreatedTodoResponse

    @Operation(summary = "투두 조회")
    @GetMapping("/{deadline}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "투두 리스트 조회 성공",
                content = [
                    Content(
                        schema = Schema(implementation = GetAllTodoResponse::class),
                    ),
                ],
            ),
        ],
    )
    fun getTodos(
        @AccessUser userId: String,
        @PathVariable deadline: LocalDate,
    ) : GetAllTodoResponse

    @Operation(summary = "투두 삭제")
    @DeleteMapping("/{todoId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "투두 삭제 성공",
            ),
        ],
    )
    fun deleteTodo(
        @AccessUser userId: String,
        @PathVariable todoId: String,
    )

    @Operation(summary = "투두 수정")
    @PutMapping("/{todoId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "투두 수정 성공",
            ),
        ],
    )
    fun updateTodo(
        @AccessUser userId: String,
        @PathVariable todoId: String,
        @RequestBody request: ModifyTodoRequest
    )

    @Operation(summary = "투두 완료")
    @PatchMapping("/{todoId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "투두 완료 성공",
            ),
        ],
    )
    fun completeTodo(
        @AccessUser userId: String,
        @PathVariable todoId: String,
    )


}
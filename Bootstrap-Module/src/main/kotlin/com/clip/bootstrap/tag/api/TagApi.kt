package com.clip.bootstrap.tag.api

import com.clip.bootstrap.common.security.annotation.AccessUser
import com.clip.bootstrap.tag.dto.CreateTagRequest
import com.clip.bootstrap.tag.dto.GetAllTagResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*


@Tag(name = "Tag", description = "Tag API")
@RequestMapping("/api/v1/tags")
interface TagApi {

    @Operation(summary = "태그 생성")
    @PostMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "태그 생성 성공"
            )
        ]
    )
    fun createTag(
        @RequestBody request: CreateTagRequest,
        @AccessUser userId: String,
    )

    @Operation(summary = "태그 리스트 조회")
    @GetMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "태그 리스트 조회 성공",
                content = [
                    Content(
                        schema = Schema(implementation = GetAllTagResponse::class),
                    ),
                ],
            ),
        ],
    )
    fun getAllTags(
        @AccessUser userId: String,
    ): GetAllTagResponse

    @Operation(summary = "태그 수정")
    @PutMapping("/{tagId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "태그 수정 성공"
            )
        ]
    )
    fun updateTag(
        @RequestBody request: CreateTagRequest,
        @AccessUser userId: String, @PathVariable tagId: String,
    )
}
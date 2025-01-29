package com.clip.bootstrap.user.api

import com.clip.bootstrap.common.security.annotation.AccessUser
import com.clip.bootstrap.user.dto.GetAllFriendRequestResponse
import com.clip.bootstrap.user.dto.GetAllFriendResponse
import com.clip.bootstrap.user.dto.FriendSearchResponse
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.*

@Tag(name = "Social", description = "Social API")
@RequestMapping("/api/v1/friends")
interface FriendApi {

    @Operation(summary = "친구 검색")
    @GetMapping("/search")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "친구 검색 리스트 조회 성공",
                content = [
                    Content(
                        schema = Schema(implementation = FriendSearchResponse::class),
                    ),
                ],
            ),
        ],
    )
    fun searchFriends(
        @AccessUser userId: String, @RequestParam nickname: String,
    ): FriendSearchResponse

    @Operation(summary = "친구 리스트 조회")
    @GetMapping
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "친구 리스트 조회 성공",
                content = [
                    Content(
                        schema = Schema(implementation = GetAllFriendResponse::class),
                    ),
                ],
            ),
        ],
    )fun getAllFriends(
        @AccessUser userId: String,
    ): GetAllFriendResponse

    @Operation(summary = "친구 추가 요청")
    @PostMapping("/{friendId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "친구 추가 성공"
            ),
            ApiResponse(
                responseCode = "400",
                description = "친구 추가 실패"
            ),
            ApiResponse(
                responseCode = "405",
                description = "이미 친구 관계"
            ),
        ]
    )
    fun addFriendRequest(
        @AccessUser userId: String, @PathVariable friendId: String,
    )

    @Operation(summary = "친구 요청 수락")
    @PatchMapping("/{requestId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "친구 요청 수락 성공"
            ),
            ApiResponse(
                responseCode = "400",
                description = "친구 요청 수락 실패"
            ),
        ]
    )
    fun acceptFriendRequest(
        @AccessUser userId: String, @PathVariable requestId: String,
    )

    @Operation(summary = "친구 요청 리스트 조회")
    @GetMapping("/requests")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "친구 요청 리스트 조회 성공",
                content = [
                    Content(
                        schema = Schema(implementation = GetAllFriendRequestResponse::class),
                    ),
                ],
            ),
        ],
    )
    fun getAllFriendRequests(
        @AccessUser userId: String,
    ): GetAllFriendRequestResponse

    @Operation(summary = "친구 삭제")
    @DeleteMapping("/{friendId}")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "친구 삭제 성공"
            ),
            ApiResponse(
                responseCode = "400",
                description = "친구 삭제 실패"
            ),
        ]
    )
    fun deleteFriend(
        @AccessUser userId: String, @PathVariable friendId: String,
    )


}
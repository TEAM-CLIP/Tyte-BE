package com.clip.bootstrap.user.dto

data class GetAllFriendResponse(
    val friends: List<FriendDetail>
) {
    data class FriendDetail(
        val userId: String,
        val nickname: String,
        val email: String
    )
}
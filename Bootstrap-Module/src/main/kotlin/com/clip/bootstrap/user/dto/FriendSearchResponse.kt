package com.clip.bootstrap.user.dto

data class FriendSearchResponse(
    val friends: List<FriendSearchDetail>
) {
    data class FriendSearchDetail(
        val userId: String,
        val nickname: String,
        val email: String,
        val isFriend: Boolean,
        val isPending: Boolean,
        val isIncoming: Boolean
    )
}
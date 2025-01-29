package com.clip.bootstrap.user.dto

class GetAllFriendRequestResponse(
    val friendRequests: List<FriendRequestDetail>
) {
    data class FriendRequestDetail(
        val requestId : String,
        val requesterInfo: RequesterInfo,
        val requestStatus: String
    )

    data class RequesterInfo(
        val userId: String,
        val nickname: String,
        val email: String
    )
}


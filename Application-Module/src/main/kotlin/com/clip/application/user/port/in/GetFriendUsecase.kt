package com.clip.application.user.port.`in`

interface GetFriendUsecase {
    fun getSearchedFriends(searched: Query.Searched): GetSearchedFriendsResponse
    fun getAllFriends(allFriend: Query.AllFriend): GetAllFriendsResponse
    fun getFriendRequests(allFriendRequest: Query.AllFriendRequest): GetFriendRequestsResponse

    sealed class Query {
        data class Searched(
            val userId: String,
            val nickname: String,
        ) : Query()

        data class AllFriend(
            val userId: String,
        ) : Query()

        data class AllFriendRequest(
            val userId: String,
        ) : Query()
    }

    data class GetSearchedFriendsResponse(
        val friends: List<FriendSearchDetail>
    )

    data class GetAllFriendsResponse(
        val friends: List<FriendDetail>
    )

    data class GetFriendRequestsResponse(
        val friendRequests: List<FriendRequestDetail>
    )

    data class FriendSearchDetail(
        val userId: String,
        val nickname: String,
        val email: String,
        val isFriend: Boolean,
        val isPending: Boolean,
        val isIncoming: Boolean
    )

    data class FriendDetail(
        val userId: String,
        val nickname: String,
        val email: String
    )

    data class FriendRequestDetail(
        val requestId: String,
        val requesterInfo: RequesterInfo,
        val requestStatus: String
    ) {
        data class RequesterInfo(
            val userId: String,
            val nickname: String,
            val email: String
        )
    }
}
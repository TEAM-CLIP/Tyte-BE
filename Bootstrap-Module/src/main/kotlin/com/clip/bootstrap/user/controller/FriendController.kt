package com.clip.bootstrap.user.controller

import com.clip.application.user.port.`in`.AddFriendUsecase
import com.clip.application.user.port.`in`.DeleteFriendUsecase
import com.clip.application.user.port.`in`.GetFriendUsecase
import com.clip.bootstrap.user.api.FriendApi
import com.clip.bootstrap.user.dto.FriendSearchResponse
import com.clip.bootstrap.user.dto.GetAllFriendRequestResponse
import com.clip.bootstrap.user.dto.GetAllFriendResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class FriendController(
    private val addFriendUsecase: AddFriendUsecase,
    private val getFriendUsecase: GetFriendUsecase,
    private val deleteFriendUsecase: DeleteFriendUsecase,
) : FriendApi {
    override fun searchFriends(userId: String, nickname: String): FriendSearchResponse{
        val response = getFriendUsecase.getSearchedFriends(GetFriendUsecase.Query.Searched(userId, nickname))
        return FriendSearchResponse(
            response.friends.map {
                FriendSearchResponse.FriendSearchDetail(
                    it.userId,
                    it.nickname,
                    it.email,
                    it.isFriend,
                    it.isPending,
                    it.isIncoming
                )
            }
        )
    }

    override fun getAllFriends(userId: String): GetAllFriendResponse {
        val response = getFriendUsecase.getAllFriends(GetFriendUsecase.Query.AllFriend(userId))
        return GetAllFriendResponse(
            response.friends.map {
                GetAllFriendResponse.FriendDetail(
                    it.userId,
                    it.nickname,
                    it.email
                )
            }
        )
    }

    override fun addFriendRequest(userId: String, friendId: String) {
        addFriendUsecase.addFriendRequest(AddFriendUsecase.Command.AddFriend(userId, friendId))
    }

    override fun acceptFriendRequest(userId: String, requestId: String) {
        addFriendUsecase.acceptFriendRequest(AddFriendUsecase.Command.AcceptFriendRequest(userId, requestId))
    }

    override fun getAllFriendRequests(userId: String): GetAllFriendRequestResponse {
        val response = getFriendUsecase.getFriendRequests(GetFriendUsecase.Query.AllFriendRequest(userId))
        return GetAllFriendRequestResponse(
            response.friendRequests.map {
                GetAllFriendRequestResponse.FriendRequestDetail(
                    it.requestId,
                    GetAllFriendRequestResponse.RequesterInfo(
                        it.requesterInfo.userId,
                        it.requesterInfo.nickname,
                        it.requesterInfo.email
                    ),
                    it.requestStatus
                )
            }
        )

    }

    override fun deleteFriend(userId: String, friendId: String) {
        deleteFriendUsecase.deleteFriend(DeleteFriendUsecase.Command.DeleteFriend(userId, friendId))
    }
}
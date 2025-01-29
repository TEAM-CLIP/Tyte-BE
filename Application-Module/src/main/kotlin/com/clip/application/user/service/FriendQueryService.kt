package com.clip.application.user.service

import com.clip.application.user.port.`in`.GetFriendUsecase
import com.clip.application.user.port.out.FriendManagementPort
import com.clip.application.user.port.out.FriendRequestManagementPort
import com.clip.application.user.port.out.UserManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.user.enums.RequestStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FriendQueryService(
    private val friendManagementPort: FriendManagementPort,
    private val friendRequestManagementPort: FriendRequestManagementPort,
    private val userManagementPort: UserManagementPort
): GetFriendUsecase {
    override fun getSearchedFriends(searched: GetFriendUsecase.Query.Searched): GetFriendUsecase.GetSearchedFriendsResponse {
        val userId = DomainId(searched.userId)
        val searchedUsers = userManagementPort.getSearchedUsersByNickname(searched.nickname, userId)
        val friendMap = friendManagementPort.getAllFriends(userId).associateBy { it.friendId }
        val friendRequestMap = friendRequestManagementPort.getAllFriendRequests(userId).associateBy { it.requesterId }
        return GetFriendUsecase.GetSearchedFriendsResponse(
            friends = searchedUsers.map {
                GetFriendUsecase.FriendSearchDetail(
                    userId = it.id.value,
                    nickname = it.nickname,
                    email = it.email,
                    isFriend = friendMap[it.id.value]?.isFriend(searched.userId) ?: false,
                    isPending = friendRequestMap[it.id.value]?.isPending() ?: false,
                    isIncoming = friendRequestMap[it.id.value]?.isIncoming(searched.userId) ?: false,
                )
            }
        )
    }

    override fun getAllFriends(allFriend: GetFriendUsecase.Query.AllFriend): GetFriendUsecase.GetAllFriendsResponse {
        val userId = DomainId(allFriend.userId)
        val friends = friendManagementPort.getAllFriends(userId)
        val friendIds = friends.map { DomainId(it.friendId) }
        val friendMap = userManagementPort.getUsersByIds(friendIds).associateBy { it.id }
        return GetFriendUsecase.GetAllFriendsResponse(
            friends = friends.map {
                GetFriendUsecase.FriendDetail(
                    userId = it.friendId,
                    nickname = friendMap[DomainId(it.friendId)]?.nickname.orEmpty(),
                    email = friendMap[DomainId(it.friendId)]?.email.orEmpty()
                )
            }
        )
    }

    override fun getFriendRequests(allFriendRequest: GetFriendUsecase.Query.AllFriendRequest): GetFriendUsecase.GetFriendRequestsResponse {
        val friendRequests = friendRequestManagementPort.getAllPendingFriendRequests(DomainId(allFriendRequest.userId), RequestStatus.PENDING)
        val friendIds = friendRequests.map { DomainId(it.requesterId) }
        val friendMap = userManagementPort.getUsersByIds(friendIds).associateBy { it.id }

        return GetFriendUsecase.GetFriendRequestsResponse(
            friendRequests = friendRequests.map {
                GetFriendUsecase.FriendRequestDetail(
                    requestId = it.id.value,
                    requesterInfo = GetFriendUsecase.FriendRequestDetail.RequesterInfo(
                        userId = it.requesterId,
                        nickname = friendMap[DomainId(it.requesterId)]?.nickname.orEmpty(),
                        email = friendMap[DomainId(it.requesterId)]?.email.orEmpty(),
                    ),
                    requestStatus = it.requestStatus.name,
                )
            }
        )
    }
}
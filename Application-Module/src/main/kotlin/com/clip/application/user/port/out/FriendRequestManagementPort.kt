package com.clip.application.user.port.out

import com.clip.application.user.exception.UserException
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.FriendRequest
import com.clip.domain.user.enums.RequestStatus

interface FriendRequestManagementPort {

    fun saveFriendRequest(friendRequest: FriendRequest) : FriendRequest

    @Throws(UserException.FriendRequestNotFoundException::class)
    fun getFriendRequestNotNull(requestId: DomainId) : FriendRequest

    fun updateFriendRequest(friendRequest: FriendRequest) : FriendRequest

    fun getAllFriendRequests(receiverId: DomainId) : List<FriendRequest>

    fun deleteFriendRequest(receiverId: DomainId, requesterId: DomainId)

    fun getFriendRequest(receiverId: DomainId, requesterId: DomainId) : FriendRequest?

    fun getAllPendingFriendRequests(receiverId: DomainId, requestStatus: RequestStatus) : List<FriendRequest>

}
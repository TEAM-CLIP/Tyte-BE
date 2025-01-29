package com.clip.domain

import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.Friend
import com.clip.domain.user.entity.FriendRequest
import com.clip.domain.user.enums.RequestStatus

object FriendFixture {
    fun createFriend(
        id: DomainId = DomainId.generate(),
        userId: String,
        friendId: String,
    ): Friend {
        return Friend(
            id = id,
            userId = userId,
            friendId = friendId
        )
    }

    fun createFriendRequest(
        id: DomainId = DomainId.generate(),
        receiverId: String,
        requesterId: String,
    ): FriendRequest {
        return FriendRequest(
            id = id,
            receiverId = receiverId,
            requesterId = requesterId,
            requestStatus = RequestStatus.PENDING
        )
    }
}
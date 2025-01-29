package com.clip.persistence.jpa.user

import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.Friend
import com.clip.domain.user.entity.FriendRequest
import com.clip.domain.user.enums.RequestStatus
import com.clip.persistence.jpa.user.entity.FriendEntity
import com.clip.persistence.jpa.user.entity.FriendRequestEntity

object FriendMapper {
    fun toFriendEntity(friend: Friend): FriendEntity =
        FriendEntity(
            id = friend.id.value,
            userId = friend.userId,
            friendId = friend.friendId,
        )

    fun toFriend(friendEntity: FriendEntity): Friend =
        Friend(
            id = DomainId(friendEntity.id),
            userId = friendEntity.userId,
            friendId = friendEntity.friendId,
        )

    fun toFriendRequestEntity(friendRequest: FriendRequest): FriendRequestEntity =
        FriendRequestEntity(
            id = friendRequest.id.value,
            receiverId = friendRequest.receiverId,
            requesterId = friendRequest.requesterId,
            requestStatus = friendRequest.requestStatus.name
        )

    fun toFriendRequest(friendRequestEntity: FriendRequestEntity): FriendRequest =
        FriendRequest(
            id = DomainId(friendRequestEntity.id),
            receiverId = friendRequestEntity.receiverId,
            requesterId = friendRequestEntity.requesterId,
            requestStatus = RequestStatus.parse(friendRequestEntity.requestStatus)
        )
}
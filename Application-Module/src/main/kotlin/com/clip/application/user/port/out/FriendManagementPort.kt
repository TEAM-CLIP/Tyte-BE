package com.clip.application.user.port.out

import com.clip.application.user.exception.UserException
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.Friend

interface FriendManagementPort {

    fun saveFriend(friend: Friend): Friend

    @Throws(UserException.FriendNotFoundException::class)
    fun getFriendNotNull(userId: DomainId, friendId: DomainId): Friend

    fun deleteFriend(friend: Friend)

    fun getAllFriends(userId: DomainId): List<Friend>

    fun getFriend(userId: DomainId, friendId: DomainId): Friend?

}
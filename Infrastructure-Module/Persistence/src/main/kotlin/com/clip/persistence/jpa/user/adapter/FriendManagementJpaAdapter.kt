package com.clip.persistence.jpa.user.adapter

import com.clip.application.user.exception.UserException
import com.clip.application.user.port.out.FriendManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.Friend
import com.clip.persistence.jpa.user.FriendMapper
import com.clip.persistence.jpa.user.repository.FriendJpaRepository
import com.clip.persistence.jpa.user.repository.deleteByUserIdAndFriendId
import com.clip.persistence.jpa.user.repository.findActiveFriendByUserIdAndFriendId
import com.clip.persistence.jpa.user.repository.findAllActiveFriendByUserId
import org.springframework.stereotype.Repository

@Repository
class FriendManagementJpaAdapter(
    private val friendJpaRepository: FriendJpaRepository
) : FriendManagementPort {
    override fun saveFriend(friend: Friend): Friend {
        val friendEntity = FriendMapper.toFriendEntity(friend)
        friendJpaRepository.save(friendEntity)
        return friend
    }

    override fun getFriendNotNull(userId: DomainId, friendId: DomainId): Friend {
        return friendJpaRepository.findActiveFriendByUserIdAndFriendId(userId.value, friendId.value)
            ?.let { FriendMapper.toFriend(it) }
            ?: throw UserException.FriendNotFoundException()
    }

    override fun deleteFriend(friend: Friend) {
        friendJpaRepository.deleteByUserIdAndFriendId(friend.userId, friend.friendId)
    }

    override fun getAllFriends(userId: DomainId): List<Friend> {
        return friendJpaRepository.findAllActiveFriendByUserId(userId.value).map { FriendMapper.toFriend(it) }
    }

    override fun getFriend(userId: DomainId, friendId: DomainId): Friend? {
        return friendJpaRepository.findActiveFriendByUserIdAndFriendId(userId.value, friendId.value)
            ?.let { FriendMapper.toFriend(it) }
            ?: return null
    }
}
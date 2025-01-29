package com.clip.persistence.jpa.user.repository

import com.clip.persistence.jpa.common.EntityStatus
import com.clip.persistence.jpa.user.entity.FriendEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FriendJpaRepository: JpaRepository<FriendEntity, String> {

    @Query(
        """
        SELECT f
        FROM FriendEntity f
        WHERE f.userId = :userId
        AND f.friendId = :friendId
        AND f.friendStatus = :status
    """
    )
    fun findByUserIdAndFriendId(userId: String, friendId: String, status: EntityStatus): FriendEntity?

    @Query(
        """
        SELECT f
        FROM FriendEntity f
        WHERE f.userId = :userId
        AND f.friendStatus = :status
    """
    )
    fun findAllByUserId(userId: String, status: EntityStatus): List<FriendEntity>

    @Modifying
    @Query(
        """
        UPDATE FriendEntity f
        SET f.friendStatus = :status,
            f.updatedAt = CURRENT_TIMESTAMP
        WHERE f.userId = :userId
        AND f.friendId = :friendId
    """
    )
    fun updateFriendStatusByUserIdAndFriendId(userId: String, friendId: String, status: EntityStatus)
}

fun FriendJpaRepository.findActiveFriendByUserIdAndFriendId(userId: String, friendId: String): FriendEntity?
= findByUserIdAndFriendId(userId, friendId, EntityStatus.ACTIVE)

fun FriendJpaRepository.findAllActiveFriendByUserId(userId: String): List<FriendEntity>
= findAllByUserId(userId, EntityStatus.ACTIVE)

fun FriendJpaRepository.deleteByUserIdAndFriendId(userId: String, friendId: String) {
    updateFriendStatusByUserIdAndFriendId(userId, friendId, EntityStatus.DELETED)
}
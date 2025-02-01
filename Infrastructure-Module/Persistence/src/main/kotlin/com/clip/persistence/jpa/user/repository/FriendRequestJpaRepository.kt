package com.clip.persistence.jpa.user.repository

import com.clip.domain.user.enums.RequestStatus
import com.clip.persistence.jpa.common.EntityStatus
import com.clip.persistence.jpa.user.entity.FriendRequestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface FriendRequestJpaRepository: JpaRepository<FriendRequestEntity, String>{
    @Query(
        """
        SELECT f
        FROM FriendRequestEntity f
        WHERE f.id = :requestId
        AND f.friendRequestStatus = :status
    """
    )
    fun findById(requestId: String, status: EntityStatus): FriendRequestEntity?

    @Query(
        """
        SELECT f
        FROM FriendRequestEntity f
        WHERE f.receiverId = :receiverId
        AND f.friendRequestStatus = :status
        AND f.requestStatus = :requestStatus
    """
    )
    fun findAllByReceiverId(receiverId: String, status: EntityStatus, requestStatus: String): List<FriendRequestEntity>

    @Query(
        """
        SELECT f
        FROM FriendRequestEntity f
        WHERE f.receiverId = :receiverId
        AND f.friendRequestStatus = :status
    """
    )
    fun findAllByReceiverId(receiverId: String, status: EntityStatus): List<FriendRequestEntity>

    @Modifying
    @Query(
        """
        UPDATE FriendRequestEntity f
        SET f.friendRequestStatus = :status,
            f.updatedAt = CURRENT_TIMESTAMP
        WHERE f.receiverId = :receiverId
        AND f.requesterId = :requesterId
    """
    )
    fun updateFriendRequestStatusByReceiverIdAndRequesterId(receiverId: String, requesterId: String, status: EntityStatus)

    @Query(
        """
        SELECT f
        FROM FriendRequestEntity f
        WHERE f.receiverId = :receiverId
        AND f.requesterId = :requesterId
        AND f.friendRequestStatus = :status
    """
    )
    fun findByReceiverIdAndRequesterId(receiverId: String, requesterId: String, status: EntityStatus): FriendRequestEntity?

    @Modifying
    @Query(
        """
        UPDATE FriendRequestEntity f
        SET f.friendRequestStatus = :status,
            f.updatedAt = CURRENT_TIMESTAMP
        WHERE f.receiverId = :deleteUserId
        OR f.requesterId = :deleteUserId
    """
    )
    fun updateAllFriendRequestStatusByUserId(deleteUserId: String, status: EntityStatus)
}

fun FriendRequestJpaRepository.findActiveFriendRequestById(id: String): FriendRequestEntity?
= findById(id, EntityStatus.ACTIVE)

fun FriendRequestJpaRepository.findAllActiveAndPendingFriendRequestByReceiverId(receiverId: String, requestStatus: String): List<FriendRequestEntity>
= findAllByReceiverId(receiverId, EntityStatus.ACTIVE, requestStatus)

fun FriendRequestJpaRepository.deleteByReceiverIdAndRequesterId(receiverId: String, requesterId: String) {
    updateFriendRequestStatusByReceiverIdAndRequesterId(receiverId, requesterId, EntityStatus.DELETED)
}

fun FriendRequestJpaRepository.findActiveFriendRequestByReceiverIdAndRequesterId(receiverId: String, requesterId: String): FriendRequestEntity?
= findByReceiverIdAndRequesterId(receiverId, requesterId, EntityStatus.ACTIVE)

fun FriendRequestJpaRepository.findAllActiveFriendRequestByReceiverId(receiverId: String): List<FriendRequestEntity>
= findAllByReceiverId(receiverId, EntityStatus.ACTIVE)

fun FriendRequestJpaRepository.deleteAllByUserId(deleteUserId: String) {
    updateAllFriendRequestStatusByUserId(deleteUserId, EntityStatus.DELETED)
}
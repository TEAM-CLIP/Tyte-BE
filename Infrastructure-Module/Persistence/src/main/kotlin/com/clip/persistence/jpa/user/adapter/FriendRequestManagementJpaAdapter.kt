package com.clip.persistence.jpa.user.adapter

import com.clip.application.user.exception.UserException
import com.clip.application.user.port.out.FriendRequestManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.FriendRequest
import com.clip.domain.user.enums.RequestStatus
import com.clip.persistence.jpa.user.FriendMapper
import com.clip.persistence.jpa.user.repository.*
import org.springframework.stereotype.Repository

@Repository
class FriendRequestManagementJpaAdapter(
    private val friendRequestJpaRepository: FriendRequestJpaRepository
): FriendRequestManagementPort {

    override fun saveFriendRequest(friendRequest: FriendRequest): FriendRequest {
        val friendRequestEntity = FriendMapper.toFriendRequestEntity(friendRequest)
        friendRequestJpaRepository.save(friendRequestEntity)
        return friendRequest
    }

    override fun getFriendRequestNotNull(requestId: DomainId): FriendRequest {
        return friendRequestJpaRepository.findActiveFriendRequestById(requestId.value)?.let { FriendMapper.toFriendRequest(it) }
            ?: throw UserException.FriendRequestNotFoundException()
    }

    override fun updateFriendRequest(friendRequest: FriendRequest): FriendRequest {
        val friendRequestEntity = FriendMapper.toFriendRequestEntity(friendRequest)
        friendRequestEntity.update()
        friendRequestJpaRepository.save(friendRequestEntity)
        return friendRequest
    }

    override fun getAllFriendRequests(receiverId: DomainId): List<FriendRequest> {
        return friendRequestJpaRepository.findAllActiveFriendRequestByReceiverId(receiverId.value).map { FriendMapper.toFriendRequest(it) }
    }

    override fun deleteFriendRequest(receiverId: DomainId, requesterId: DomainId) {
        friendRequestJpaRepository.deleteByReceiverIdAndRequesterId(receiverId.value, requesterId.value)
    }

    override fun getFriendRequest(receiverId: DomainId, requesterId: DomainId): FriendRequest? {
        return friendRequestJpaRepository.findActiveFriendRequestByReceiverIdAndRequesterId(receiverId.value, requesterId.value)?.let { FriendMapper.toFriendRequest(it) }
    }

    override fun getAllPendingFriendRequests(receiverId: DomainId, requestStatus: RequestStatus): List<FriendRequest> {
        return friendRequestJpaRepository.findAllActiveAndPendingFriendRequestByReceiverId(receiverId.value, requestStatus.name).map { FriendMapper.toFriendRequest(it) }
    }

    override fun deleteAllFriendRequests(deleteUserId: DomainId) {
        friendRequestJpaRepository.deleteAllByUserId(deleteUserId.value)
    }
}
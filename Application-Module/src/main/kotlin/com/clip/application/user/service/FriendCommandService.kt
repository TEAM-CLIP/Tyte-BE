package com.clip.application.user.service

import com.clip.application.user.exception.UserException
import com.clip.application.user.port.`in`.AddFriendUsecase
import com.clip.application.user.port.`in`.DeleteFriendUsecase
import com.clip.application.user.port.out.FriendManagementPort
import com.clip.application.user.port.out.FriendRequestManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.Friend
import com.clip.domain.user.entity.FriendRequest
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class FriendCommandService(private val friendManagementPort: FriendManagementPort,
    private val friendRequestManagementPort: FriendRequestManagementPort): AddFriendUsecase, DeleteFriendUsecase {

    override fun addFriendRequest(command: AddFriendUsecase.Command.AddFriend) {
        friendManagementPort.getFriend(DomainId(command.userId), DomainId(command.friendId))
            ?.let { throw UserException.FriendAlreadyExistsException() }

        friendRequestManagementPort.getFriendRequest(DomainId(command.userId), DomainId(command.friendId))
            ?.let { throw UserException.FriendRequestAlreadyExistsException() }

        FriendRequest.create(receiverId = command.userId, requesterId = command.friendId)
            .also { friendRequestManagementPort.saveFriendRequest(it) }
    }


    override fun acceptFriendRequest(command: AddFriendUsecase.Command.AcceptFriendRequest) {
        val friendRequest = friendRequestManagementPort.getFriendRequestNotNull(DomainId(command.requestId))
        friendRequest.accept()
        friendRequestManagementPort.updateFriendRequest(friendRequest)
        val friend = Friend(userId = command.receiverId, friendId = friendRequest.requesterId)
        friendManagementPort.saveFriend(friend)
    }

    override fun deleteFriend(command: DeleteFriendUsecase.Command.DeleteFriend) {
        friendManagementPort.getFriendNotNull(DomainId(command.userId), DomainId(command.friendId))
            .also { friendManagementPort.deleteFriend(it) }
        friendRequestManagementPort.deleteFriendRequest(DomainId(command.userId), DomainId(command.friendId))
    }
}
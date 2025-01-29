package com.clip.application.user

import com.clip.application.user.port.out.FriendManagementPort
import com.clip.application.user.port.out.FriendRequestManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.Friend
import com.clip.domain.user.entity.FriendRequest
import com.clip.domain.user.enums.RequestStatus

class FriendMockManager(
    private val friendManagementPort: FriendManagementPort,
    private val friendRequestManagementPort: FriendRequestManagementPort
) {
    fun settingFriend(
        userId: String,
        friendId: String
    ): String =
        settingFriendWithFriendDomain(
            userId = userId,
            friendId = friendId,
        ).id.value

    fun settingFriendRequest(
        receiverId: String,
        requesterId: String,
    ): String =
        settingFriendRequestWithFriendRequestDomain(
            receiverId = receiverId,
            requesterId = requesterId,
        ).id.value

    fun settingFriendWithFriendDomain(
        userId: String = DomainId.generate().value,
        friendId: String = DomainId.generate().value,
    ) = friendManagementPort.saveFriend(
        Friend(
            id = DomainId.generate(),
            userId = userId,
            friendId = friendId,
        )
    )

    fun settingFriendRequestWithFriendRequestDomain(
        receiverId: String,
        requesterId: String,
        requestStatus: String = "PENDING",
    ) = friendRequestManagementPort.saveFriendRequest(
        FriendRequest(
            id = DomainId.generate(),
            receiverId = receiverId,
            requesterId = requesterId,
            requestStatus = RequestStatus.parse(requestStatus),
        )
    )
}
package com.clip.application.user.service

import com.clip.application.user.exception.UserException
import com.clip.application.user.port.`in`.AddFriendUsecase
import com.clip.application.user.port.`in`.DeleteFriendUsecase
import com.clip.application.user.port.out.FriendManagementPort
import com.clip.application.user.port.out.FriendRequestManagementPort
import com.clip.domain.FriendFixture
import com.clip.domain.UserFixture
import com.clip.domain.common.DomainId
import com.clip.domain.user.enums.RequestStatus
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.*

class FriendCommandServiceTest : BehaviorSpec({
    val mockFriendManagementPort = mockk<FriendManagementPort>()
    val mockFriendRequestManagementPort = mockk<FriendRequestManagementPort>()

    val friendCommandService = FriendCommandService(mockFriendManagementPort, mockFriendRequestManagementPort)

    given("사용자가 친구를 추가할 때") {
        val user = UserFixture.createUser()
        val friendUser = UserFixture.createUser()
        val friend = FriendFixture.createFriend(userId = user.id.value, friendId = friendUser.id.value)
        val command = AddFriendUsecase.Command.AddFriend(user.id.value, friendUser.id.value)
        every { mockFriendManagementPort.getFriend(DomainId(command.userId), DomainId(command.friendId)) } returns friend
        `when`("이미 친구인 경우") {
            then("예외가 발생한다.") {
                shouldThrow <UserException.FriendAlreadyExistsException> {
                    friendCommandService.addFriendRequest(command)
                }
            }
        }

        val friendRequest = FriendFixture.createFriendRequest(receiverId = user.id.value, requesterId = friendUser.id.value)
        every { mockFriendManagementPort.getFriend(DomainId(command.userId), DomainId(command.friendId)) } returns null
        every { mockFriendRequestManagementPort.getFriendRequest(DomainId(command.userId), DomainId(command.friendId)) } returns friendRequest
        `when`("친구 요청이 이미 있는 경우") {
            then("예외가 발생한다.") {
                shouldThrow <UserException.FriendRequestAlreadyExistsException> {
                    friendCommandService.addFriendRequest(command)
                }
            }
        }

        every { mockFriendManagementPort.getFriend(DomainId(command.userId), DomainId(command.friendId)) } returns null
        every { mockFriendRequestManagementPort.getFriendRequest(DomainId(command.userId), DomainId(command.friendId)) } returns null
        every { mockFriendRequestManagementPort.saveFriendRequest(any()) } returns friendRequest
        `when`("친구 요청이 없고 친구 관계가 아닌 경우") {
            friendCommandService.addFriendRequest(command)
            then("친구 요청을 저장한다.") {
                verify {
                    mockFriendRequestManagementPort.saveFriendRequest(
                        match { it.receiverId == user.id.value && it.requesterId == friendUser.id.value }
                    )
                }
            }
        }
    }

    given("친구 요청을 수락할 때") {
        val user = UserFixture.createUser()
        val friendUser = UserFixture.createUser()
        val friend = FriendFixture.createFriend(userId = user.id.value, friendId = friendUser.id.value)
        val friendRequest = FriendFixture.createFriendRequest(receiverId = user.id.value, requesterId = friendUser.id.value)
        val command = AddFriendUsecase.Command.AcceptFriendRequest(friendRequest.receiverId, friendRequest.id.value)
        every { mockFriendRequestManagementPort.getFriendRequestNotNull(DomainId(command.requestId)) } returns friendRequest
        every { mockFriendRequestManagementPort.updateFriendRequest(any()) } returns friendRequest
        every { mockFriendManagementPort.saveFriend(any()) } returns friend
        `when`("친구 요청을 수락하면") {
            friendCommandService.acceptFriendRequest(command)
            then("친구 요청을 업데이트하고 친구를 저장한다.") {
                verify {
                    mockFriendRequestManagementPort.updateFriendRequest(friendRequest)
                    mockFriendManagementPort.saveFriend(
                        match { it.userId == friendRequest.receiverId && it.friendId == friendRequest.requesterId }
                    )
                    friendRequest.requestStatus shouldBe RequestStatus.ACCEPTED
                }
            }
        }
    }

    given("친구를 삭제할 때") {
        val user = UserFixture.createUser()
        val friendUser = UserFixture.createUser()
        val friend = FriendFixture.createFriend(userId = user.id.value, friendId = friendUser.id.value)
        val command = DeleteFriendUsecase.Command.DeleteFriend(user.id.value, friendUser.id.value)
        every { mockFriendManagementPort.getFriendNotNull(DomainId(command.userId), DomainId(command.friendId)) } returns friend
        every { mockFriendManagementPort.deleteFriend(any()) } just Runs
        every { mockFriendRequestManagementPort.deleteFriendRequest(DomainId(command.userId), DomainId(command.friendId)) } just Runs
        `when`("친구를 삭제하면") {
            friendCommandService.deleteFriend(command)
            then("친구를 삭제한다.") {
                verify {
                    mockFriendManagementPort.deleteFriend(friend)
                    mockFriendRequestManagementPort.deleteFriendRequest(DomainId(command.userId), DomainId(command.friendId))
                }
            }
        }
    }

})
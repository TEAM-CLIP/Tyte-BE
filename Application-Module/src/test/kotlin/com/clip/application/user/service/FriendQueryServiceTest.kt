package com.clip.application.user.service

import com.clip.application.user.port.`in`.GetFriendUsecase
import com.clip.application.user.port.out.FriendManagementPort
import com.clip.application.user.port.out.FriendRequestManagementPort
import com.clip.application.user.port.out.UserManagementPort
import com.clip.domain.FriendFixture
import com.clip.domain.UserFixture
import com.clip.domain.common.DomainId
import com.clip.domain.user.enums.RequestStatus
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk


class FriendQueryServiceTest: BehaviorSpec({
    val mockFriendManagementPort = mockk<FriendManagementPort>()
    val mockFriendRequestManagementPort = mockk<FriendRequestManagementPort>()
    val mockUserManagementPort = mockk<UserManagementPort>()

    val friendQueryService = FriendQueryService(mockFriendManagementPort, mockFriendRequestManagementPort, mockUserManagementPort)

    given("사용자가 친구를 조회할 때") {
        val user = UserFixture.createUser()
        val friendUser = UserFixture.createUser()
        val friend = FriendFixture.createFriend(userId = user.id.value, friendId = friendUser.id.value)
        val friendRequest = FriendFixture.createFriendRequest(receiverId = user.id.value, requesterId = friendUser.id.value)
        val nickname = "nickname"
        val query = GetFriendUsecase.Query.Searched(user.id.value, nickname)
        val searchedUsers = listOf(friendUser)
        every { mockUserManagementPort.getSearchedUsersByNickname(query.nickname, DomainId(query.userId)) } returns searchedUsers
        every { mockFriendManagementPort.getAllFriends(DomainId(query.userId)) } returns listOf(friend)
        every { mockFriendRequestManagementPort.getAllFriendRequests(DomainId(query.userId)) } returns listOf(friendRequest)
        `when`("사용자 아이디와 친구 닉네임을 입력하면") {
            val response = friendQueryService.getSearchedFriends(query)
            then("친구를 조회한다") {
                response.friends.size shouldBe 1
                response.friends[0].userId shouldBe friendUser.id.value
                response.friends[0].nickname shouldBe friendUser.nickname
                response.friends[0].email shouldBe friendUser.email
                response.friends[0].isFriend shouldBe true
                response.friends[0].isPending shouldBe true
                response.friends[0].isIncoming shouldBe true
            }
        }
    }

    given("사용자의 모든 친구를 조회할 때") {
        val user = UserFixture.createUser()
        val friendUser = UserFixture.createUser()
        val friend = FriendFixture.createFriend(userId = user.id.value, friendId = friendUser.id.value)
        val friendIds = listOf(DomainId(friend.friendId))
        val query = GetFriendUsecase.Query.AllFriend(user.id.value)
        every { mockFriendManagementPort.getAllFriends(DomainId(query.userId)) } returns listOf(friend)
        every { mockUserManagementPort.getUsersByIds(friendIds) } returns listOf(friendUser)
        `when`("사용자 아이디를 입력하면") {
            val response = friendQueryService.getAllFriends(query)
            then("사용자의 모든 친구를 조회한다") {
                response.friends.size shouldBe 1
                response.friends[0].userId shouldBe friendUser.id.value
                response.friends[0].nickname shouldBe friendUser.nickname
                response.friends[0].email shouldBe friendUser.email
            }
        }
    }

    given("사용자의 친구 요청을 조회할 때") {
        val user = UserFixture.createUser()
        val friendUser = UserFixture.createUser()
        val friendRequest = FriendFixture.createFriendRequest(receiverId = user.id.value, requesterId = friendUser.id.value)
        val friendIds = listOf(DomainId(friendRequest.requesterId))
        val query = GetFriendUsecase.Query.AllFriendRequest(user.id.value)
        every { mockFriendRequestManagementPort.getAllPendingFriendRequests(DomainId(query.userId), RequestStatus.PENDING)} returns listOf(friendRequest)
        every { mockUserManagementPort.getUsersByIds(friendIds) } returns listOf(friendUser)
        `when`("사용자 아이디를 입력하면") {
            val response = friendQueryService.getFriendRequests(query)
            then("사용자의 친구 요청을 조회한다") {
                response.friendRequests.size shouldBe 1
                response.friendRequests[0].requestId shouldBe friendRequest.id.value
                response.friendRequests[0].requesterInfo.userId shouldBe friendUser.id.value
                response.friendRequests[0].requesterInfo.nickname shouldBe friendUser.nickname
                response.friendRequests[0].requesterInfo.email shouldBe friendUser.email
            }
        }
    }

})
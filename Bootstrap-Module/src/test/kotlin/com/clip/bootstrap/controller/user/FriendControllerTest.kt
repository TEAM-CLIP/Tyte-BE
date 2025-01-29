package com.clip.bootstrap.controller.user

import com.clip.application.user.port.`in`.AddFriendUsecase
import com.clip.application.user.port.`in`.DeleteFriendUsecase
import com.clip.application.user.port.`in`.GetFriendUsecase
import com.clip.bootstrap.ControllerSupporter
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

class FriendControllerTest : ControllerSupporter(){

    @MockBean
    private lateinit var addFriendUsecase: AddFriendUsecase

    @MockBean
    private lateinit var getFriendUsecase: GetFriendUsecase

    @MockBean
    private lateinit var deleteFriendUsecase: DeleteFriendUsecase

    @Test
    fun searchFriendsTest(){
        // given
        val userId = userMockManager.settingUser()
        val nickname = "nickname"
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val query = GetFriendUsecase.Query.Searched(userId, nickname)
        val friendSearchDetail = GetFriendUsecase.FriendSearchDetail("userId", "nickname", "email", true, true, true)
        val response = GetFriendUsecase.GetSearchedFriendsResponse(listOf(friendSearchDetail))
        given(getFriendUsecase.getSearchedFriends(query)).willReturn(response)
        // when
        val result = mockMvc.get("/api/v1/friends/search?nickname=$nickname") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")  // 인증 토큰 추가
        }

        // then
        result.andExpect {
            status { isOk() }
            jsonPath("$.friends[0].userId") { value(friendSearchDetail.userId) }
            jsonPath("$.friends[0].nickname") { value(friendSearchDetail.nickname) }
            jsonPath("$.friends[0].email") { value(friendSearchDetail.email) }
            jsonPath("$.friends[0].isFriend") { value(friendSearchDetail.isFriend) }
            jsonPath("$.friends[0].isPending") { value(friendSearchDetail.isPending) }
            jsonPath("$.friends[0].isIncoming") { value(friendSearchDetail.isIncoming) }
        }
    }

    @Test
    fun getAllFriendsTest(){
        // given
        val userId = userMockManager.settingUser()
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val query = GetFriendUsecase.Query.AllFriend(userId)
        val friendDetail = GetFriendUsecase.FriendDetail("userId", "nickname", "email")
        val response = GetFriendUsecase.GetAllFriendsResponse(listOf(friendDetail))
        given(getFriendUsecase.getAllFriends(query)).willReturn(response)
        // when
        val result = mockMvc.get("/api/v1/friends") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")  // 인증 토큰 추가
        }

        // then
        result.andExpect {
            status { isOk() }
            jsonPath("$.friends[0].userId") { value(friendDetail.userId) }
            jsonPath("$.friends[0].nickname") { value(friendDetail.nickname) }
            jsonPath("$.friends[0].email") { value(friendDetail.email) }
        }
    }

    @Test
    fun addFriendRequestTest(){
        // given
        val userId = userMockManager.settingUser()
        val friendId = userMockManager.settingUser()
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val command = AddFriendUsecase.Command.AddFriend(userId, friendId)
        // when
        mockMvc.post("/api/v1/friends/$friendId") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")
        }
        // then
        verify(addFriendUsecase).addFriendRequest(command)
    }

    @Test
    fun acceptFriendRequestTest(){
        // given
        val userId = userMockManager.settingUser()
        val requestId = "requestId"
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val command = AddFriendUsecase.Command.AcceptFriendRequest(userId, requestId)
        // when
        mockMvc.patch("/api/v1/friends/$requestId") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")
        }
        // then
        verify(addFriendUsecase).acceptFriendRequest(command)
    }

    @Test
    fun getAllFriendRequestsTest(){
        // given
        val userId = userMockManager.settingUser()
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val query = GetFriendUsecase.Query.AllFriendRequest(userId)
        val friendRequestDetail = GetFriendUsecase.FriendRequestDetail("requestId", GetFriendUsecase.FriendRequestDetail.RequesterInfo("userId", "nickname", "email"), "requestStatus")
        val response = GetFriendUsecase.GetFriendRequestsResponse(listOf(friendRequestDetail))
        given(getFriendUsecase.getFriendRequests(query)).willReturn(response)
        // when
        val result = mockMvc.get("/api/v1/friends/requests") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")  // 인증 토큰 추가
        }

        // then
        result.andExpect {
            status { isOk() }
            jsonPath("$.friendRequests[0].requestId") { value(friendRequestDetail.requestId) }
            jsonPath("$.friendRequests[0].requesterInfo.userId") { value(friendRequestDetail.requesterInfo.userId) }
            jsonPath("$.friendRequests[0].requesterInfo.nickname") { value(friendRequestDetail.requesterInfo.nickname) }
            jsonPath("$.friendRequests[0].requesterInfo.email") { value(friendRequestDetail.requesterInfo.email) }
            jsonPath("$.friendRequests[0].requestStatus") { value(friendRequestDetail.requestStatus) }
        }
    }

    @Test
    fun deleteFriendTest(){
        // given
        val userId = userMockManager.settingUser()
        val friendId = userMockManager.settingUser()
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val command = DeleteFriendUsecase.Command.DeleteFriend(userId, friendId)
        // when
        mockMvc.delete("/api/v1/friends/$friendId") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")
        }
        // then
        verify(deleteFriendUsecase).deleteFriend(command)
    }
}
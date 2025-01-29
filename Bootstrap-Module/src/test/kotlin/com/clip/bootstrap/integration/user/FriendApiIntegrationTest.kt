package com.clip.bootstrap.integration.user

import com.asap.bootstrap.IntegrationSupporter
import com.clip.application.user.FriendMockManager
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post

class FriendApiIntegrationTest: IntegrationSupporter() {

    @Autowired
    private lateinit var friendMockManager: FriendMockManager

    @Test
    fun searchFriendsTest() {
        //given
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val nickname = "nickname"
        // when
        val response = mockMvc.get("/api/v1/friends/search?nickname=$nickname") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")
        }
        // then
        response.andExpect {
            status { isOk() }
            jsonPath("$.friends") {
                exists()
                isArray()
                jsonPath("$.friends[0].userId") {
                    exists()
                    isString()
                }
                jsonPath("$.friends[0].nickname") {
                    exists()
                    isString()
                }
                jsonPath("$.friends[0].email") {
                    exists()
                    isString()
                }
                jsonPath("$.friends[0].isFriend") {
                    exists()
                    isBoolean()
                }
                jsonPath("$.friends[0].isPending") {
                    exists()
                    isBoolean()
                }
                jsonPath("$.friends[0].isIncoming") {
                    exists()
                    isBoolean()
                }
            }
        }
    }

    @Test
    fun getAllFriendsTest() {
        //given
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val friendId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = friendId)
        friendMockManager.settingFriend(userId = userId, friendId = friendId)
        friendMockManager.settingFriendRequest(receiverId = userId, requesterId = friendId)
        // when
        val response = mockMvc.get("/api/v1/friends") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")
        }
        // then
        response.andExpect {
            status { isOk() }
            jsonPath("$.friends") {
                exists()
                isArray()
                jsonPath("$.friends[0].userId") {
                    exists()
                    isString()
                }
                jsonPath("$.friends[0].nickname") {
                    exists()
                    isString()
                }
                jsonPath("$.friends[0].email") {
                    exists()
                    isString()
                }
            }
        }
    }

    @Test
    fun addFriendRequestTest() {
        //given
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val friendId = userMockManager.settingUser()
        // when
        val response = mockMvc.post("/api/v1/friends/$friendId") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")
        }
        // then
        response.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun acceptFriendRequestTest() {
        //given
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val friendId = userMockManager.settingUser()
        val requestId = friendMockManager.settingFriendRequest(receiverId = userId, requesterId = friendId)
        // when
        val response = mockMvc.patch("/api/v1/friends/$requestId") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")
        }
        // then
        response.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun getAllFriendRequestsTest() {
        //given
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val friendId = userMockManager.settingUser()
        friendMockManager.settingFriendRequest(receiverId = userId, requesterId = friendId)
        // when
        val response = mockMvc.get("/api/v1/friends/requests") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")
        }
        // then
        response.andExpect {
            status { isOk() }
            jsonPath("$.friendRequests") {
                exists()
                isArray()
                jsonPath("$.friendRequests[0].requestId") {
                    exists()
                    isString()
                }
                jsonPath("$.friendRequests[0].requesterInfo.userId") {
                    exists()
                    isString()
                }
                jsonPath("$.friendRequests[0].requesterInfo.nickname") {
                    exists()
                    isString()
                }
                jsonPath("$.friendRequests[0].requesterInfo.email") {
                    exists()
                    isString()
                }
                jsonPath("$.friendRequests[0].requestStatus") {
                    exists()
                    isString()
                }
            }
        }
    }

    @Test
    fun deleteFriendTest() {
        //given
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val friendId = userMockManager.settingUser()
        friendMockManager.settingFriend(userId = userId, friendId = friendId)
        friendMockManager.settingFriendRequest(receiverId = userId, requesterId = friendId)
        // when
        val response = mockMvc.delete("/api/v1/friends/$friendId") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")
        }
        // then
        response.andExpect {
            status { isOk() }
        }
    }
}
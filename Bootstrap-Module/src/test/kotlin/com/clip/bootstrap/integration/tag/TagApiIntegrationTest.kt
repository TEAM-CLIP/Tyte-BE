package com.clip.bootstrap.integration.tag

import com.asap.bootstrap.IntegrationSupporter
import com.clip.application.tag.TagMockManager
import com.clip.bootstrap.tag.dto.CreateTagRequest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

class TagApiIntegrationTest : IntegrationSupporter(){

    @Autowired
    private lateinit var tagMockManager: TagMockManager

    @Test
    fun createTag(){
        //given
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val request = CreateTagRequest("name", "color")
        // when
        val response =
            mockMvc.post("/api/v1/tags") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)
                header("Authorization", "Bearer $accessToken")
            }
        // then
        response.andExpect {
            status { isOk() }
        }
    }

    @Test
    fun getAllTags(){
        //given
        val userId = userMockManager.settingUser()
        userMockManager.settingUserAuth(userId = userId)
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val tagId = tagMockManager.settingTag(userId = userId)
        //when
        val response =
            mockMvc.get("/api/v1/tags") {
                contentType = MediaType.APPLICATION_JSON
                header("Authorization", "Bearer $accessToken")
            }
        //then
        response.andExpect {
            status { isOk() }
            jsonPath("$.tags") {
                exists()
                isArray()
                jsonPath("$.tags[0].tagId") {
                    exists()
                    isString()
                    isNotEmpty()
                    value(tagId)
                }
                jsonPath("$.tags[0].name") {
                    exists()
                    isString()
                    isNotEmpty()
                }
                jsonPath("$.tags[0].color") {
                    exists()
                    isString()
                    isNotEmpty()
                }
            }
        }
    }

    @Test
    fun updateTag(){
        //given
        val userId = userMockManager.settingUser()
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val tagId = tagMockManager.settingTag(userId = userId)
        val request = CreateTagRequest("name", "color")
        //when
        val response =
            mockMvc.put("/api/v1/tags/$tagId") {
                contentType = MediaType.APPLICATION_JSON
                content = objectMapper.writeValueAsString(request)

                header("Authorization", "Bearer $accessToken")
            }
        //then
        response.andExpect {
            status { isOk() }
        }
    }

//    @Test
//    fun deleteTag(){
//        //given
//        val userId = userMockManager.settingUser()
//        val accessToken = jwtMockManager.generateAccessToken(userId)
//        val tagId = tagMockManager.settingTag(userId)
//        //when
//        val response =
//            mockMvc.delete("/api/v1/tags/$tagId") {
//                contentType = MediaType.APPLICATION_JSON
//                header("Authorization", "Bearer $accessToken")
//            }
//        //then
//        response.andExpect {
//            status { isOk() }
//        }
//    }
}
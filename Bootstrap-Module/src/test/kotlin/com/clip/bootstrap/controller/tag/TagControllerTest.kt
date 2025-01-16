package com.clip.bootstrap.controller.tag

import com.clip.application.tag.port.`in`.usecase.CreateTagUsecase
import com.clip.application.tag.port.`in`.usecase.GetTagUsecase
import com.clip.application.tag.port.`in`.usecase.UpdateTagUsecase
import com.clip.bootstrap.ControllerSupporter
import com.clip.bootstrap.tag.dto.CreateTagRequest
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put

class TagControllerTest: ControllerSupporter() {

    @MockBean
    private lateinit var createTagUsecase: CreateTagUsecase

    @MockBean
    private lateinit var getTagUseCase: GetTagUsecase

    @MockBean
    private lateinit var updateTagUsecase: UpdateTagUsecase

    @Test
    fun createTagTest() {
        // given
        val request = CreateTagRequest("name", "color")
        val userId = userMockManager.settingUser()
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val command = CreateTagUsecase.Command(userId, request.name, request.color)

        // when
        mockMvc.post("/api/v1/tags") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
            header("Authorization", "Bearer $accessToken")  // 인증 토큰 추가
        }

        // then
        verify(createTagUsecase).create(command)
    }

    @Test
    fun getAllTagsTest() {
        // given
        val userId = userMockManager.settingUser()
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val tagDetail = GetTagUsecase.TagDetail("tagId", "name", "color")
        val tags = listOf(
            tagDetail
        )
        val response = GetTagUsecase.GetAllResponse(tags)
        given(getTagUseCase.getAll(GetTagUsecase.GetAllQuery(userId))).willReturn(response)
        // when
        val result = mockMvc.get("/api/v1/tags") {
            contentType = MediaType.APPLICATION_JSON
            header("Authorization", "Bearer $accessToken")  // 인증 토큰 추가
        }

        // then
        result.andExpect {
            status { isOk() }
            jsonPath("$.tags[0].tagId") { value(tagDetail.tagId) }
            jsonPath("$.tags[0].name") { value(tagDetail.name) }
            jsonPath("$.tags[0].color") { value(tagDetail.color) }
        }
    }

    @Test
    fun updateTagTest() {
        // given
        val userId = userMockManager.settingUser()
        val accessToken = jwtMockManager.generateAccessToken(userId)
        val request = CreateTagRequest("name", "color")
        val command = UpdateTagUsecase.Command("tagId", userId, request.name, request.color)

        // when
        mockMvc.put("/api/v1/tags/tagId") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(request)
            header("Authorization", "Bearer $accessToken")
        }

        // then
        verify(updateTagUsecase).update(command)
    }
}
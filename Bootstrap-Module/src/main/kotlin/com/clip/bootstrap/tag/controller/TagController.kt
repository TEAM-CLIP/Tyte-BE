package com.clip.bootstrap.tag.controller

import com.clip.application.tag.port.`in`.CreateTagUsecase
import com.clip.application.tag.port.`in`.GetTagUseCase
import com.clip.application.tag.port.`in`.UpdateTagUsecase
import com.clip.bootstrap.tag.api.TagApi
import com.clip.bootstrap.tag.dto.CreateTagRequest
import com.clip.bootstrap.tag.dto.GetAllTagResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class TagController(
    private val createTagUsecase: CreateTagUsecase,
    private val getTagUseCase: GetTagUseCase,
    private val updateTagUsecase: UpdateTagUsecase
): TagApi {
    override fun createTag(request: CreateTagRequest, userId: String) {
        createTagUsecase.create(
            CreateTagUsecase.Command(
                userId,
                request.name,
                request.color
            )
        )
    }

    override fun getAllTags(userId: String): GetAllTagResponse {
        val response = getTagUseCase.getAll(GetTagUseCase.GetAllQuery(userId))
        return GetAllTagResponse(
            response.tags.map {
                GetAllTagResponse.TagDetail(
                    it.tagId,
                    it.name,
                    it.color
                )
            }
        )
    }

    override fun updateTag(request: CreateTagRequest, userId: String, tagId: String) {
        updateTagUsecase.update(
            UpdateTagUsecase.Command(
                userId,
                tagId,
                request.name,
                request.color
            )
        )
    }
}
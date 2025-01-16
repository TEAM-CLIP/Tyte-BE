package com.clip.bootstrap.tag.controller

import com.clip.application.tag.port.`in`.usecase.CreateTagUsecase
import com.clip.application.tag.port.`in`.usecase.DeleteTagUsecase
import com.clip.application.tag.port.`in`.usecase.GetTagUsecase
import com.clip.application.tag.port.`in`.usecase.UpdateTagUsecase
import com.clip.bootstrap.tag.api.TagApi
import com.clip.bootstrap.tag.dto.CreateTagRequest
import com.clip.bootstrap.tag.dto.GetAllTagResponse
import org.springframework.web.bind.annotation.RestController

@RestController
class TagController(
    private val createTagUsecase: CreateTagUsecase,
    private val getTagUseCase: GetTagUsecase,
    private val updateTagUsecase: UpdateTagUsecase,
    private val deleteTagUsecase: DeleteTagUsecase
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
        val response = getTagUseCase.getAll(GetTagUsecase.GetAllQuery(userId))
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

    override fun updateTag(request: CreateTagRequest, tagId: String, userId: String) {
        updateTagUsecase.update(
            UpdateTagUsecase.Command(
                tagId,
                userId,
                request.name,
                request.color
            )
        )
    }

    override fun deleteTag(tagId: String, userId: String) {
        deleteTagUsecase.delete(
            DeleteTagUsecase.Command(
                tagId,
                userId
            )
        )
    }
}
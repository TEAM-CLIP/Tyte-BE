package com.clip.application.tag.service

import com.clip.application.tag.port.`in`.usecase.GetTagUsecase
import com.clip.application.tag.port.out.TagManagementPort
import com.clip.domain.common.DomainId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TagQueryService(
    private val tagManagementPort: TagManagementPort
) : GetTagUsecase {
    override fun getAll(getAllQuery: GetTagUsecase.GetAllQuery): GetTagUsecase.GetAllResponse {
        val tags = tagManagementPort.getAllTagBy(userId = DomainId(getAllQuery.userId))
        return GetTagUsecase.GetAllResponse(
            tags = tags.map {
                GetTagUsecase.TagDetail(
                    tagId = it.id.value,
                    name = it.name,
                    color = it.color,
                )
            }
        )
    }
}

package com.clip.application.tag.service

import com.clip.application.tag.port.`in`.GetTagUseCase
import org.springframework.stereotype.Service

@Service
class TagQueryService : GetTagUseCase {
    override fun getAll(getAllQuery: GetTagUseCase.GetAllQuery): GetTagUseCase.GetAllResponse {
        TODO("Not yet implemented")
    }
}
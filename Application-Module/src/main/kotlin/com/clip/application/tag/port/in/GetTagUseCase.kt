package com.clip.application.tag.port.`in`

interface GetTagUseCase {
    fun getAll(getAllQuery: GetAllQuery): GetAllResponse

    data class GetAllQuery(
        val userId: String
    )

    data class GetAllResponse(
        val tags: List<TagDetail>
    )

    data class TagDetail(
        val tagId: String,
        val name: String,
        val color: String
    )
}
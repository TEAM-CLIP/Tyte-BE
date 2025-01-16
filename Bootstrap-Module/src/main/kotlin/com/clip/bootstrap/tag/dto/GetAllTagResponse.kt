package com.clip.bootstrap.tag.dto


data class GetAllTagResponse(
    val tags : List<TagDetail>
) {
    data class TagDetail(
        val tagId: String,
        val name: String,
        val color: String
    )
}
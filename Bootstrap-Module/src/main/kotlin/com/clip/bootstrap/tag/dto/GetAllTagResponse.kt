package com.clip.bootstrap.tag.dto


data class GetAllTagResponse(
    val tages : List<TagDetail>
) {
    data class TagDetail(
        val tagId: String,
        val name: String,
        val color: String
    )
}
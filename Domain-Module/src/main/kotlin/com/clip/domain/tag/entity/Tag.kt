package com.clip.domain.tag.entity

import com.clip.domain.common.DomainId

data class Tag(
    val id: DomainId = DomainId.generate(),
    val userId: String,
    val name: String,
    val color: String,
)  {
    companion object {
        fun createDefaultTags(userId: DomainId): List<Tag> {
            return listOf(
                Tag(
                    id = DomainId.generate(),
                    userId = userId.value,
                    name = "학습",
                    color = "FF0000"
                ),
                Tag(
                    id = DomainId.generate(),
                    userId = userId.value,
                    name = "여가",
                    color = "F0E68C"
                ),
                Tag(
                    id = DomainId.generate(),
                    userId = userId.value,
                    name = "건강",
                    color = "00FFFF"
                )
            )
        }
    }

    fun update(name: String, color: String) {
        this.name = name
        this.color = color
    }
}
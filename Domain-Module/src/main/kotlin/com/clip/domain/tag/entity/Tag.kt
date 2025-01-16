package com.clip.domain.tag.entity

import com.clip.domain.common.AggregateRoot
import com.clip.domain.common.DomainId

class Tag(
    id: DomainId = DomainId.generate(),
    val userId: DomainId,
    var name: String,
    var color: String,
): AggregateRoot<Tag>(id) {
    companion object {
        fun default(userId: DomainId): List<Tag> {
            return listOf(
                Tag(
                    id = DomainId.generate(),
                    userId = userId,
                    name = "학습",
                    color = "FF0000"
                ),
                Tag(
                    id = DomainId.generate(),
                    userId = userId,
                    name = "여가",
                    color = "F0E68C"
                ),
                Tag(
                    id = DomainId.generate(),
                    userId = userId,
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
package com.clip.domain

import com.clip.domain.common.DomainId
import com.clip.domain.tag.entity.Tag

object TagFixture {
    fun createTag(
        tagId: DomainId = DomainId.generate(),
        userId: DomainId = DomainId.generate(),
        name: String = "name",
        color: String = "color",
    ): Tag {
        return Tag(
            id = tagId,
            userId = userId,
            name = name,
            color = color,
        )
    }
}
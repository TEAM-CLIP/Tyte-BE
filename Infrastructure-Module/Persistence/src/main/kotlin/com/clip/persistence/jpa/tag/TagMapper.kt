package com.clip.persistence.jpa.tag

import com.clip.domain.common.DomainId
import com.clip.domain.tag.entity.Tag
import com.clip.persistence.jpa.tag.entity.TagEntity

object TagMapper {
    fun toTagEntity(tag: Tag): TagEntity =
        TagEntity(
            id = tag.id.value,
            userId = tag.userId.value,
            name = tag.name,
            color = tag.color
        )

    fun toTag(tagEntity: TagEntity): Tag =
        Tag(
            id = DomainId(tagEntity.id),
            userId = DomainId(tagEntity.userId),
            name = tagEntity.name,
            color = tagEntity.color
        )
}
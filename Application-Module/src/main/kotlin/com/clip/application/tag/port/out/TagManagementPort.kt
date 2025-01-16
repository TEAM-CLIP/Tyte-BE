package com.clip.application.tag.port.out

import com.clip.domain.common.DomainId
import com.clip.domain.tag.entity.Tag

interface TagManagementPort {

    fun getAllTagBy(userId: DomainId): List<Tag>

    fun saveTag(tag: Tag): Tag

    fun getTagNotNull(
        tagId: DomainId,
        userId: DomainId
    ): Tag

    fun saveAll(tags: List<Tag>): List<Tag>

    fun deleteTag(tag: Tag)
}
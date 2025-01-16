package com.clip.application.tag

import com.clip.application.tag.port.out.TagManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.tag.entity.Tag

class TagMockManager(
    private val tagManagementPort: TagManagementPort
) {
    fun settingTag(
        tagId: String = DomainId.generate().value,
        userId: String,
    ): String =
        settingTagWithTagDomain(
            tagId = tagId,
            userId = userId
        ).id.value

    fun settingTagWithTagDomain(
        tagId: String = DomainId.generate().value,
        userId: String
    ) = tagManagementPort.saveTag(
        Tag(
            id = DomainId(tagId),
            userId = DomainId(userId),
            name = "name",
            color = "color"
        )
    )

}

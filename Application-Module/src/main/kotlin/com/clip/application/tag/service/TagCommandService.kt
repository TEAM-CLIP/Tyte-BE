package com.clip.application.tag.service

import com.clip.application.tag.port.`in`.usecase.CreateTagUsecase
import com.clip.application.tag.port.`in`.usecase.DeleteTagUsecase
import com.clip.application.tag.port.`in`.usecase.UpdateTagUsecase
import com.clip.application.tag.port.out.TagManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.tag.entity.Tag
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TagCommandService(
    private val tagManagementPort: TagManagementPort
) : CreateTagUsecase, UpdateTagUsecase, DeleteTagUsecase {
    override fun create(command: CreateTagUsecase.Command) {
        val tag = Tag(
            userId = DomainId(command.userId),
            name = command.name,
            color = command.color
        )
        tagManagementPort.saveTag(tag)
    }

    override fun update(command: UpdateTagUsecase.Command) {
        val tag = tagManagementPort.getTagNotNull(
            tagId = DomainId(command.tagId),
            userId = DomainId(command.userId)
        )
        tag.update(
            name = command.name,
            color = command.color
        )

        tagManagementPort.saveTag(tag)
    }

    override fun delete(command: DeleteTagUsecase.Command) {
        val tag = tagManagementPort.getTagNotNull(
            tagId = DomainId(command.tagId),
            userId = DomainId(command.userId)
        )
        tagManagementPort.deleteTag(tag)
    }
}
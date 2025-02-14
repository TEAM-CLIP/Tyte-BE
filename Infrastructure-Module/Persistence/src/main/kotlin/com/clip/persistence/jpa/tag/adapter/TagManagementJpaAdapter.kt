package com.clip.persistence.jpa.tag.adapter

import com.clip.application.tag.exception.TagException
import com.clip.application.tag.port.out.TagManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.tag.entity.Tag
import com.clip.persistence.jpa.tag.TagMapper
import com.clip.persistence.jpa.tag.repository.*
import org.springframework.stereotype.Repository

@Repository
class TagManagementJpaAdapter(
        private val tagJpaRepository: TagJpaRepository
) : TagManagementPort {
    override fun getAllTagBy(userId: DomainId): List<Tag> {
        return tagJpaRepository.findAllActiveTagByUserId(userId.value)
            .map { TagMapper.toTag(it) }
    }

    override fun saveTag(tag: Tag): Tag {
        val tagEntity = TagMapper.toTagEntity(tag)
        tagJpaRepository.save(tagEntity)
        return tag
    }

    override fun updateTag(tag: Tag): Tag {
        val tagEntity = TagMapper.toTagEntity(tag)
        tagEntity.update()
        tagJpaRepository.save(tagEntity)
        return tag
    }

    override fun getTagNotNull(tagId: DomainId, userId: DomainId): Tag =
        tagJpaRepository.findActiveTagByIdAndUserId(tagId.value, userId.value)?.let {
            TagMapper.toTag(it)
        } ?: throw TagException.TagNotFoundException()

    override fun saveAll(tags: List<Tag>): List<Tag> {
        val tagEntities = tags.map { TagMapper.toTagEntity(it) }
        tagJpaRepository.saveAll(tagEntities)
        return tags
    }

    override fun deleteTag(tag: Tag) {
        tagJpaRepository.deleteByIdAndUserId(
            tagId = tag.id.value,
            userId = tag.userId.value
        )
    }

    override fun getAllTagByIds(tagIds: List<DomainId>): List<Tag> =
        tagJpaRepository.findAllActiveTagByIds(tagIds.map { it.value })
            .map { TagMapper.toTag(it) }
}
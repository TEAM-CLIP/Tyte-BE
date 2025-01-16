package com.clip.persistence.jpa.tag.repository

import com.clip.persistence.jpa.common.EntityStatus
import com.clip.persistence.jpa.tag.entity.TagEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface TagJpaRepository: JpaRepository<TagEntity, String> {

    @Query(
        """
        SELECT t
        FROM TagEntity t
        WHERE t.userId = :userId
        AND t.tagStatus = :status
    """
    )
    fun findAllBy(userId: String, status: EntityStatus): List<TagEntity>

    @Query(
        """
        SELECT t
        FROM TagEntity t
        WHERE t.id = :tagId
        AND t.userId = :userId
        AND t.tagStatus = :status
    """
    )
    fun findById(tagId: String, userId: String, status: EntityStatus): TagEntity?

    @Modifying
    @Query(
        """
        UPDATE TagEntity t
        SET t.tagStatus = :status,
            t.updatedAt = CURRENT_TIMESTAMP
        WHERE t.id = :tagId
        AND t.userId = :userId
    """
    )
    fun updateTagEntityStatusByIdAndUserId(tagId: String, userId: String, status: EntityStatus)
}

fun TagJpaRepository.findAllActiveTagByUserId(
    userId: String
): List<TagEntity> = findAllBy(userId, EntityStatus.ACTIVE)

fun TagJpaRepository.findActiveTagByIdAndUserId(
    tagId: String,
    userId: String
): TagEntity? = findById(tagId, userId, EntityStatus.ACTIVE)

fun TagJpaRepository.deleteByIdAndUserId(
    tagId: String,
    userId: String
) {
    updateTagEntityStatusByIdAndUserId(tagId, userId, EntityStatus.DELETED)
}
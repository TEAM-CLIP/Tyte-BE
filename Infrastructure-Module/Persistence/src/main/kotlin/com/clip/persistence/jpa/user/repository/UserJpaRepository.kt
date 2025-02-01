package com.clip.persistence.jpa.user.repository

import com.clip.persistence.jpa.common.EntityStatus
import com.clip.persistence.jpa.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface UserJpaRepository : JpaRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u WHERE u.id = :userId AND u.userStatus = :status")
    fun findByUserId(userId: String, status: EntityStatus): UserEntity?

    @Query("SELECT u FROM UserEntity u WHERE u.email = :email AND u.userStatus = :status")
    fun findByEmail (email: String, status: EntityStatus): UserEntity?

    @Query("SELECT u FROM UserEntity u WHERE u.id IN :ids AND u.userStatus = :status")
    fun findAllByIds(ids: List<String>, status: EntityStatus): List<UserEntity>

    @Query("SELECT u FROM UserEntity u WHERE u.nickname LIKE %:nickname% AND u.userStatus = :status")
    fun findAllByNicknameContaining(nickname: String, status: EntityStatus): List<UserEntity>

    @Modifying
    @Query(
        """
        UPDATE UserEntity u
        SET u.userStatus = :status,
            u.updatedAt = CURRENT_TIMESTAMP
        WHERE u.id = :userId
    """
    )
    fun updateUserStatusById(userId: String, status: EntityStatus)

}

fun UserJpaRepository.findActiveUserById(userId: String): UserEntity?
        = findByUserId(userId, EntityStatus.ACTIVE)

fun UserJpaRepository.findActiveUserByEmail(email: String): UserEntity?
        = findByEmail(email, EntityStatus.ACTIVE)

fun UserJpaRepository.findAllActiveUserByIds(ids: List<String>): List<UserEntity>
        = findAllByIds(ids, EntityStatus.ACTIVE)

fun UserJpaRepository.findAllActiveUserByNicknameContaining(nickname: String): List<UserEntity>
        = findAllByNicknameContaining(nickname, EntityStatus.ACTIVE)

fun UserJpaRepository.deleteUserById(userId: String)
        = updateUserStatusById(userId, EntityStatus.DELETED)

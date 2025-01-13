package com.clip.persistence.jpa.user.repository

import com.clip.persistence.jpa.user.entity.UserAuthEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserAuthJpaRepository : JpaRepository<UserAuthEntity, String> {

    fun findBySocialIdAndLoginProvider(
        socialId: String,
        loginProvider: String,
    ): UserAuthEntity?

    fun existsBySocialIdAndLoginProvider(
        socialId: String,
        loginProvider: String,
    ): Boolean

    fun findByUserId(userId: String): UserAuthEntity?

    @Query(
        """
        SELECT u
        FROM UserAuthEntity u
        WHERE u.user.email = :email
        """
    )
    fun findByUserEmail(email: String): UserAuthEntity?
}

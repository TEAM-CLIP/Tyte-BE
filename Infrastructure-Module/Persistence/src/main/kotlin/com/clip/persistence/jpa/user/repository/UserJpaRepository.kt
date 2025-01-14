package com.clip.persistence.jpa.user.repository

import com.clip.persistence.jpa.user.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserJpaRepository : JpaRepository<UserEntity, String> {

    fun findByEmail (email: String): UserEntity?
}

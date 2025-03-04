package com.clip.persistence.jpa.user.entity

import com.clip.persistence.jpa.common.BaseEntity
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Index
import jakarta.persistence.Table

@Entity
@Table(
    name = "user_token",
    indexes = [
        Index(name = "uk_token_index", columnList = "token", unique = true),
    ],
)
class UserTokenEntity(
    id: String,
    userId : String?,
    token: String,
) : BaseEntity(id) {
    @Column(nullable = false, unique = true, length = 500)
    val token: String = token

    @Column(length = 500, name = "user_id")
    val userId: String? = userId
}

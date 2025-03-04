package com.clip.persistence.jpa.user.entity

import com.clip.persistence.jpa.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(
    name = "user_auth",
    indexes = [
        Index(name = "idx_social_id_index", columnList = "socialId"),
    ],
)
class UserAuthEntity(
    id: String,
    userId: String,
    socialId: String?,
    loginProvider: String,
    passwordHash: String?
) : BaseEntity(id) {
    @Column(nullable = false)
    val userId: String = userId

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", insertable = false, updatable = false)
    lateinit var user: UserEntity

    val socialId: String? = socialId

    @Column(nullable = false)
    val loginProvider: String = loginProvider

    val passwordHash: String? = passwordHash

}

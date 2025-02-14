package com.clip.persistence.jpa.user.entity

import com.clip.persistence.jpa.common.BaseEntity
import com.clip.persistence.jpa.common.EntityStatus
import jakarta.persistence.*

@Entity
@Table(name = "user")
class UserEntity(
    id: String,
    nickname: String,
    email: String
) : BaseEntity(id) {
    @Column(nullable = false)
    val nickname: String = nickname

    @Column(
        columnDefinition = "varchar(100)",
    )
    val email: String = email

    @Enumerated(EnumType.STRING)
    @Column(
        name = "user_status",
        nullable = false,
        columnDefinition = "varchar(20)",
    )
    var userStatus: EntityStatus = EntityStatus.ACTIVE

}

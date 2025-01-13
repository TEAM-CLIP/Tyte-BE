package com.clip.persistence.jpa.user.entity

import com.clip.persistence.jpa.common.AggregateRoot
import jakarta.persistence.*

@Entity
@Table(name = "user")
class UserEntity(
    id: String,
    nickname: String,
    email: String
) : AggregateRoot<UserEntity>(id) {
    @Column(nullable = false)
    val nickname: String = nickname

    @Column(
        columnDefinition = "varchar(100)",
    )
    val email: String = email

}

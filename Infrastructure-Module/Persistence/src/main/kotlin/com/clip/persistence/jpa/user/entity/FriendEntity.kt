package com.clip.persistence.jpa.user.entity

import com.clip.persistence.jpa.common.BaseEntity
import com.clip.persistence.jpa.common.EntityStatus
import jakarta.persistence.*

@Entity
@Table(name = "friend")
class FriendEntity(
    id: String,
    userId: String,
    friendId: String
) : BaseEntity(id) {

    @Column(nullable = false)
    val userId: String = userId

    @Column(nullable = false)
    val friendId: String = friendId

    @Enumerated(EnumType.STRING)
    @Column(
        name = "friend_status",
        nullable = false,
        columnDefinition = "varchar(20)",
    )
    var friendStatus: EntityStatus = EntityStatus.ACTIVE

}
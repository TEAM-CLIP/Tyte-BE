package com.clip.persistence.jpa.user.entity

import com.clip.persistence.jpa.common.BaseEntity
import com.clip.persistence.jpa.common.EntityStatus
import jakarta.persistence.*

@Entity
@Table(name = "friend_request")
class FriendRequestEntity(
    id: String,
    receiverId: String,
    requesterId: String,
    requestStatus: String,
) : BaseEntity(id) {

    @Column(nullable = false)
    val receiverId: String = receiverId

    @Column(nullable = false)
    val requesterId: String = requesterId

    @Column(nullable = false)
    val requestStatus: String = requestStatus

    @Enumerated(EnumType.STRING)
    @Column(
        name = "friend_request_status",
        nullable = false,
        columnDefinition = "varchar(20)",
    )
    var friendRequestStatus: EntityStatus = EntityStatus.ACTIVE

}

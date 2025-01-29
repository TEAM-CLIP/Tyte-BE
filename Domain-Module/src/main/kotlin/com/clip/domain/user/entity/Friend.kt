package com.clip.domain.user.entity

import com.clip.domain.common.DomainId

class Friend(
    val id : DomainId = DomainId.generate(),
    val userId: String,
    val friendId: String,
) {
    fun isFriend(userId: String): Boolean {
        return this.userId == userId
    }
}


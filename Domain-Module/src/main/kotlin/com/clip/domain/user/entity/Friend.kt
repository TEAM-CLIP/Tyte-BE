package com.clip.domain.user.entity

import com.clip.domain.common.AggregateRoot
import com.clip.domain.common.DomainId

class Friend(
    id : DomainId = DomainId.generate(),
    val userId: String,
    val friendId: String,
): AggregateRoot<Friend>(id){
    fun isFriend(userId: String): Boolean {
        return this.userId == userId
    }
}


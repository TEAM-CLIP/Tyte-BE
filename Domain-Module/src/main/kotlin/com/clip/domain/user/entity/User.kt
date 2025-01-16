package com.clip.domain.user.entity

import com.clip.domain.common.AggregateRoot
import com.clip.domain.common.DomainId
import com.clip.domain.user.event.UserEvent

class User(
    id: DomainId,
    val nickname: String,
    val email: String
) : AggregateRoot<User>(id) {
    companion object {
        fun create(
            id: DomainId = DomainId.generate(),
            nickname: String,
            email: String
        ): User =
            User(id, nickname, email).also {
                it.registerEvent(UserEvent.UserCreatedEvent(it))
            }
    }

}

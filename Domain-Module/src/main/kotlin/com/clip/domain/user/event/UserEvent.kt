package com.clip.domain.user.event

import com.clip.domain.common.DomainEvent
import com.clip.domain.user.entity.User

sealed class UserEvent : DomainEvent<User> {
    data class UserCreatedEvent(
        val user: User,
    ) : UserEvent()
}

package com.clip.domain.user.entity

import com.clip.domain.common.AggregateRoot
import com.clip.domain.common.DomainId

class UserToken(
    id: DomainId = DomainId.generate(),
    val userId: DomainId? = null,
    val token: String,
): AggregateRoot<UserToken>(id) {
}

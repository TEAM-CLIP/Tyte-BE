package com.clip.domain.user.entity

import com.clip.domain.common.DomainId

data class User(
    val id: DomainId = DomainId.generate(),
    val nickname: String,
    val email: String
)

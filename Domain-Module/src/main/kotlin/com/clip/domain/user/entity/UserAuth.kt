package com.clip.domain.user.entity

import com.clip.domain.common.DomainId
import com.clip.domain.user.enums.LoginProvider

data class UserAuth(
    val id: DomainId = DomainId.generate(),
    val userId: DomainId,
    val socialId: String? = null,
    val loginProvider: LoginProvider,
    val passwordHash: String? = null
)

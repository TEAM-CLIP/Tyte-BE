package com.clip.application.user.vo

import com.clip.domain.user.enums.LoginProvider

data class AuthInfo(
    val loginProvider: LoginProvider,
    val socialId: String,
    val email: String?
)

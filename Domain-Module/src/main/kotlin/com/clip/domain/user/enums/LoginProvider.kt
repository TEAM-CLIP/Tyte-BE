package com.clip.domain.user.enums

import com.clip.common.exception.DefaultException

enum class LoginProvider {
    GOOGLE,    // 소셜 로그인
    APPLE,     // 소셜 로그인
    BASIC;     // 일반 로그인

    companion object {
        private val socialProviders = setOf(GOOGLE, APPLE) // 소셜 로그인 제공자만 포함

        fun parseSocialProvider(value: String): LoginProvider {
            return socialProviders.firstOrNull { it.name == value }
                ?: throw DefaultException.InvalidArgumentException("유효하지 않은 소셜 로그인 제공자입니다.")
        }

        fun parse(value: String): LoginProvider {
            return entries.firstOrNull { it.name == value }
                ?: throw DefaultException.InvalidArgumentException("유효하지 않은 로그인 제공자입니다.")
        }

    }
}

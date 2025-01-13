package com.clip.security.crypt.user

import at.favre.lib.crypto.bcrypt.BCrypt

object PasswordManager {

    private const val COST_FACTOR = 12 // 작업 비용 설정 (기본값: 12)

    fun hashPassword(password: String): String {
        return BCrypt.withDefaults().hashToString(COST_FACTOR, password.toCharArray())
    }

    fun verifyPassword(rawPassword: String, hashedPassword: String?): Boolean {
        return BCrypt.verifyer().verify(rawPassword.toCharArray(), hashedPassword).verified
    }
}
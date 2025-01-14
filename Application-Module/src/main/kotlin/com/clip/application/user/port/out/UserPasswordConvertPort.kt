package com.clip.application.user.port.out

interface UserPasswordConvertPort {
    fun hashPassword(password: String): String

    fun verifyPassword(rawPassword: String, encodedPassword: String?): Boolean
}
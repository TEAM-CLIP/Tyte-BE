package com.clip.security.crypt.user

import com.clip.application.user.port.out.UserPasswordConvertPort
import org.springframework.stereotype.Component

@Component
class UserPasswordConvertAdapter(): UserPasswordConvertPort {
    override fun hashPassword(password: String): String {
        return PasswordManager.hashPassword(password)
    }

    override fun verifyPassword(rawPassword: String, encodedPassword: String?): Boolean {
        return PasswordManager.verifyPassword(rawPassword, encodedPassword)
    }
}
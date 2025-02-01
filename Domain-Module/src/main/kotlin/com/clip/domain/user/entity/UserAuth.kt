package com.clip.domain.user.entity

import com.clip.domain.common.AggregateRoot
import com.clip.domain.common.DomainId
import com.clip.domain.user.enums.LoginProvider

class UserAuth(
    id: DomainId,
    val userId: DomainId,
    val socialId: String? = null,
    val loginProvider: LoginProvider,
    val passwordHash: String? = null
): AggregateRoot<UserAuth>(id) {
    companion object {
        fun createBasicAuth(
            id: DomainId = DomainId.generate(),
            userId: DomainId,
            passwordHash: String
        ): UserAuth =
            UserAuth(id, userId, loginProvider = LoginProvider.BASIC, passwordHash = passwordHash)

        fun createSocialAuth(
            id: DomainId = DomainId.generate(),
            userId: DomainId,
            socialId: String,
            loginProvider: LoginProvider
        ): UserAuth =
            UserAuth(id, userId, socialId, loginProvider)
    }

    fun copy(
        userId: DomainId = this.userId,
        socialId: String? = this.socialId,
        loginProvider: LoginProvider = this.loginProvider,
        passwordHash: String? = this.passwordHash
    ): UserAuth {
        return UserAuth(id, userId, socialId, loginProvider, passwordHash)
    }
}

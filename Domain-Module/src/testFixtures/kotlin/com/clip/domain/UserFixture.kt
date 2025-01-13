package com.clip.domain


import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.User
import com.clip.domain.user.entity.UserAuth
import com.clip.domain.user.enums.LoginProvider

object UserFixture {
    fun createUser(
        id: DomainId = DomainId.generate(),
        nickname: String = "nickname",
    ): User {
        return User(
            id = id,
            nickname = nickname,
            email = "email",
        )
    }

    fun createUserAuth(
        userId: String = "userId",
        socialId: String = "socialId",
        loginProvider: LoginProvider = LoginProvider.GOOGLE,
    ): UserAuth =
        UserAuth(
            userId = DomainId(userId),
            socialId = socialId,
            loginProvider = loginProvider,
        )
}

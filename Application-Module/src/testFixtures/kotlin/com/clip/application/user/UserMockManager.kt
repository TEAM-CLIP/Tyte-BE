package com.clip.application.user


import com.clip.application.user.port.out.UserAuthManagementPort
import com.clip.application.user.port.out.UserManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.User
import com.clip.domain.user.entity.UserAuth
import com.clip.domain.user.enums.LoginProvider

class UserMockManager(
    private val userManagementPort: UserManagementPort,
    private val userAuthManagementPort: UserAuthManagementPort,
) {
    fun settingUser(
        userId: String = DomainId.generate().value,
        nickname: String = "nickname",
    ): String =
        settingUserWithUserDomain(
            userId = userId,
            nickname = nickname,
        ).id.value

    fun settingUserWithUserDomain(
        userId: String = DomainId.generate().value,
        nickname: String = "nickname",
    ) = userManagementPort.saveUser(
        User(
            id = DomainId(userId),
            nickname = nickname,
            email = "email",
        ),
    )

    fun settingUserAuth(
        userId: String,
        socialId: String = "socialId",
        provider: String = "GOOGLE",
    ) {
        userAuthManagementPort.saveUserAuth(
            UserAuth(
                userId = DomainId(userId),
                socialId = socialId,
                loginProvider = LoginProvider.parse(provider),
            ),
        )
    }
}

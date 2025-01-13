package com.clip.persistence.jpa.user

import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.User
import com.clip.domain.user.entity.UserAuth
import com.clip.domain.user.entity.UserToken
import com.clip.domain.user.enums.LoginProvider
import com.clip.persistence.jpa.user.entity.UserAuthEntity
import com.clip.persistence.jpa.user.entity.UserEntity
import com.clip.persistence.jpa.user.entity.UserTokenEntity

object UserMapper {
    fun toUserEntity(user: User): UserEntity =
        UserEntity(
            id = user.id.value,
            nickname = user.nickname,
            email = user.email
        )

    fun toUser(userEntity: UserEntity): User =
        User(
            id = DomainId(userEntity.id),
            nickname = userEntity.nickname,
            email = userEntity.email
        )

    fun toUserAuthEntity(userAuth: UserAuth): UserAuthEntity =
        UserAuthEntity(
            id = userAuth.id.value,
            socialId = userAuth.socialId,
            loginProvider = userAuth.loginProvider.name,
            userId = userAuth.userId.value,
            passwordHash = userAuth.passwordHash
        )

    fun toUserAuth(userAuthEntity: UserAuthEntity): UserAuth =
        UserAuth(
            id = DomainId(userAuthEntity.id),
            socialId = userAuthEntity.socialId,
            loginProvider = LoginProvider.parse(userAuthEntity.loginProvider),
            userId = DomainId(userAuthEntity.userId),
            passwordHash = userAuthEntity.passwordHash
        )

    fun toUserTokenEntity(userToken: UserToken): UserTokenEntity =
        UserTokenEntity(
            id = userToken.id.value,
            token = userToken.token,
            userId = userToken.userId?.value,
        )

}

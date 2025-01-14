package com.clip.application.user.port.out

import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.UserAuth
import com.clip.domain.user.enums.LoginProvider

interface UserAuthManagementPort {

    fun getUserAuth(
        socialId: String,
        loginProvider: LoginProvider
    ): UserAuth?


    fun isExistsUserAuth(
        socialId: String,
        loginProvider: LoginProvider
    ): Boolean


    fun saveUserAuth(
        userAuth: UserAuth
    ): UserAuth

    fun getNotNull(userId: DomainId): UserAuth

    fun getUserAuthByEmail(email: String): UserAuth?

    fun delete(userAuth: UserAuth)
}
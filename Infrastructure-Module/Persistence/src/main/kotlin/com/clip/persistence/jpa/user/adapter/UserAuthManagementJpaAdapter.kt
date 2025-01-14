package com.clip.persistence.jpa.user.adapter

import com.clip.application.user.exception.UserException
import com.clip.application.user.port.out.UserAuthManagementPort
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.UserAuth
import com.clip.domain.user.enums.LoginProvider
import com.clip.persistence.jpa.user.UserMapper
import com.clip.persistence.jpa.user.repository.UserAuthJpaRepository
import org.springframework.stereotype.Repository

@Repository
class UserAuthManagementJpaAdapter(
    private val userAuthJpaRepository: UserAuthJpaRepository,
) : UserAuthManagementPort {

    override fun getUserAuth(
        socialId: String,
        loginProvider: LoginProvider,
    ): UserAuth? =
        userAuthJpaRepository
            .findBySocialIdAndLoginProvider(socialId, loginProvider.name)
            ?.let { UserMapper.toUserAuth(it) }

    override fun isExistsUserAuth(
        socialId: String,
        loginProvider: LoginProvider,
    ): Boolean = userAuthJpaRepository.existsBySocialIdAndLoginProvider(socialId, loginProvider.name)

    override fun saveUserAuth(userAuth: UserAuth): UserAuth {
        val userAuthEntity = UserMapper.toUserAuthEntity(userAuth)
        userAuthJpaRepository.save(userAuthEntity)
        return userAuth
    }

    override fun getNotNull(userId: DomainId): UserAuth {
        val userAuthEntity =
            userAuthJpaRepository.findByUserId(userId.value)
                ?: throw UserException.UserAuthNotFoundException()
        return UserMapper.toUserAuth(userAuthEntity)
    }

    override fun getUserAuthByEmail(email: String): UserAuth? {
        return userAuthJpaRepository.findByUserEmail(email)?.let { UserMapper.toUserAuth(it) }
    }

    override fun delete(userAuth: UserAuth) {
        userAuthJpaRepository.deleteById(userAuth.id.value)
    }

}

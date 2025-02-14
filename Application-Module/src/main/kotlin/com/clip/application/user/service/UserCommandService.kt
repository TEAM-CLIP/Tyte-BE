package com.clip.application.user.service

import com.clip.application.user.exception.UserException
import com.clip.application.user.port.`in`.DeleteUserUsecase
import com.clip.application.user.port.`in`.RegisterUserUsecase
import com.clip.application.user.port.`in`.VerifyUserUsecase
import com.clip.application.user.port.out.*
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.User
import com.clip.domain.user.entity.UserAuth
import com.clip.domain.user.entity.UserToken
import com.clip.domain.user.enums.LoginProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserCommandService (
    private val userTokenConvertPort: UserTokenConvertPort,
    private val userAuthManagementPort: UserAuthManagementPort,
    private val userManagementPort: UserManagementPort,
    private val userTokenManagementPort: UserTokenManagementPort,
    private val userPasswordConvertPort: UserPasswordConvertPort,
    private val friendManagementPort: FriendManagementPort,
    private val friendRequestManagementPort: FriendRequestManagementPort
    ) : RegisterUserUsecase, VerifyUserUsecase, DeleteUserUsecase{

    override fun registerSocialUser(command: RegisterUserUsecase.Command.Social): RegisterUserUsecase.Response {
        if (!userTokenManagementPort.isExistsToken(command.registerToken)) {
            throw UserException.UserPermissionDeniedException("존재하지 않는 가입 토큰입니다.")
        }
        val userClaims = userTokenConvertPort.resolveRegisterToken(command.registerToken)
        if (userAuthManagementPort.isExistsUserAuth(userClaims.socialId, userClaims.loginProvider)) {
            throw UserException.UserAlreadyRegisteredException()
        }
        val registerUser = User.create(nickname = command.nickname, email = userClaims.email)
        val userAuth = UserAuth.createSocialAuth(
            userId = registerUser.id,
            socialId = userClaims.socialId,
            loginProvider = userClaims.loginProvider
        )

        userManagementPort.saveUser(registerUser)
        userAuthManagementPort.saveUserAuth(userAuth)

        val accessToken = userTokenConvertPort.generateAccessToken(registerUser)
        val refreshToken = userTokenConvertPort.generateRefreshToken(registerUser)

        userTokenManagementPort.saveUserToken(UserToken(userId = registerUser.id, token = refreshToken))

        return RegisterUserUsecase.Response(accessToken, refreshToken)
    }

    override fun registerBasicUser(command: RegisterUserUsecase.Command.Basic): RegisterUserUsecase.Response {
        val hashedPassword = userPasswordConvertPort.hashPassword(command.password)
        val registerUser = User.create(nickname = command.nickname, email = command.email)

        val userAuth = UserAuth.createBasicAuth(userId = registerUser.id, passwordHash = hashedPassword)
        userManagementPort.saveUser(registerUser)
        userAuthManagementPort.saveUserAuth(userAuth)

        val accessToken = userTokenConvertPort.generateAccessToken(registerUser)
        val refreshToken = userTokenConvertPort.generateRefreshToken(registerUser)

        userTokenManagementPort.saveUserToken(UserToken(userId = registerUser.id, token = refreshToken))

        return RegisterUserUsecase.Response(accessToken, refreshToken)
    }

    override fun verifyUser(command: VerifyUserUsecase.Command): VerifyUserUsecase.Response {
        val userAuth = userAuthManagementPort.getUserAuthByEmail(command.email)
            ?: return VerifyUserUsecase.Response(false)

        return when (userAuth.loginProvider) {
            LoginProvider.BASIC -> VerifyUserUsecase.Response(true)
            else -> throw UserException.UserAuthAlreadyExistsException()
        }
    }

    override fun delete(command: DeleteUserUsecase.Command) {
        val user = userManagementPort.getUserNotNull(DomainId(command.userId))
        val userAuth = userAuthManagementPort.getNotNull(user.id)
        userAuthManagementPort.delete(userAuth)
        userManagementPort.delete(user)
        friendManagementPort.deleteAllFriends(user.id)
        friendRequestManagementPort.deleteAllFriendRequests(user.id)
    }
}

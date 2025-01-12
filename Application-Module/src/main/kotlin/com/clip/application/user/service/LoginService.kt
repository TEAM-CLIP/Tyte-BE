package com.clip.application.user.service

import com.clip.application.user.exception.UserException
import com.clip.application.user.port.`in`.LoginUsecase
import com.clip.application.user.port.out.*
import com.clip.application.user.vo.AuthInfo
import com.clip.common.exception.DefaultException
import com.clip.domain.user.entity.UserAuth
import com.clip.domain.user.entity.UserToken
import com.clip.domain.user.enums.LoginProvider
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
@Transactional
class LoginService(
    private val userAuthManagementPort: UserAuthManagementPort,
    private val authInfoRetrievePort: AuthInfoRetrievePort,
    private val userTokenConvertPort: UserTokenConvertPort,
    private val userManagementPort: UserManagementPort,
    private val userTokenManagementPort: UserTokenManagementPort,
    private val userPasswordConvertPort: UserPasswordConvertPort
) : LoginUsecase {

    override fun socialLogin(command: LoginUsecase.Command.Social): LoginUsecase.Response {
        val authInfo = authInfoRetrievePort.getAuthInfo(LoginProvider.parseSocialProvider(command.provider), command.accessToken)
        (authInfo.email ?: command.email)?.let { verifyAccountExistence(it, authInfo.loginProvider)}
        val userAuth = userAuthManagementPort.getUserAuth(authInfo.socialId, authInfo.loginProvider)

        return userAuth?.let { handleExistingUser(it) } ?: handleNewUser(authInfo, command.email)
    }

    override fun basicLogin(command: LoginUsecase.Command.Basic): LoginUsecase.Response {
        val user = userManagementPort.getUserNotNullByEmail(command.email)
        val userAuth = userAuthManagementPort.getNotNull(user.id)


        verifyPassword(command.password, userAuth)
        return handleExistingUser(userAuth)
    }

    private fun handleExistingUser(userAuth: UserAuth): LoginUsecase.Response {
        val user = userManagementPort.getUser(userAuth.userId)
            ?: throw DefaultException.InvalidStateException("사용자 인증정보만 존재합니다. - ${userAuth.userId}")

        return user.let {
            val accessToken = userTokenConvertPort.generateAccessToken(it)
            val refreshToken = userTokenConvertPort.generateRefreshToken(it)
            userTokenManagementPort.saveUserToken(UserToken(token = refreshToken))
            LoginUsecase.Success(accessToken, refreshToken)
        }
    }

    private fun handleNewUser(authInfo: AuthInfo, email: String?): LoginUsecase.Response {
        val registerToken = userTokenConvertPort.generateRegisterToken(
            authInfo.socialId,
            authInfo.loginProvider.name,
            authInfo.emailOrThrow(email)
        )
        userTokenManagementPort.saveUserToken(UserToken(token = registerToken))
        return LoginUsecase.NonRegistered(registerToken)
    }

    private fun AuthInfo.emailOrThrow(commandEmail: String?): String =
        email ?: commandEmail ?: throw IllegalStateException("이메일 정보가 없습니다. - $this")

    private fun verifyPassword(password: String, userAuth: UserAuth) {
        if (!userPasswordConvertPort.verifyPassword(password, userAuth.passwordHash))
            throw DefaultException.InvalidStateException("비밀번호가 일치하지 않습니다.")
    }

    private fun verifyAccountExistence(email: String, loginProvider: LoginProvider) {
        val userAuth = userAuthManagementPort.getUserAuthByEmail(email)
            ?: return

        if (userAuth.loginProvider != loginProvider) {
            throw UserException.UserAuthAlreadyExistsException()
        }
    }
}

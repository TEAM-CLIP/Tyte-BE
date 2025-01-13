package com.clip.application.user.service


import com.clip.application.user.exception.UserException
import com.clip.application.user.port.`in`.LoginUsecase
import com.clip.application.user.port.out.*
import com.clip.application.user.vo.AuthInfo
import com.clip.common.exception.DefaultException
import com.clip.domain.UserFixture
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.UserAuth
import com.clip.domain.user.enums.LoginProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class LoginServiceTest :
    BehaviorSpec({

        val mockUserAuthManagementPort = mockk<UserAuthManagementPort>()
        val mockAuthInfoRetrievePort = mockk<AuthInfoRetrievePort>()
        val mockUserManagementPort = mockk<UserManagementPort>()
        val mockUserTokenConvertPort = mockk<UserTokenConvertPort>()
        val mockUserTokenManagementPort = mockk<UserTokenManagementPort>(relaxed = true)
        val mockUserPasswordConvertPort = mockk<UserPasswordConvertPort>()

        val socialLoginService =
            LoginService(
                mockUserAuthManagementPort,
                mockAuthInfoRetrievePort,
                mockUserTokenConvertPort,
                mockUserManagementPort,
                mockUserTokenManagementPort,
                mockUserPasswordConvertPort
            )

        given("소셜 로그인에서 요청한 사용자가") {
            var command = LoginUsecase.Command.Social(LoginProvider.GOOGLE.name, "registered")
            val authInfo = AuthInfo(LoginProvider.GOOGLE, "socialId", "email")
            val getUserAuth =
                UserAuth(
                    userId = DomainId.generate(),
                    socialId = "socialId",
                    loginProvider = LoginProvider.GOOGLE,
                )
            val getUser = UserFixture.createUser(getUserAuth.userId)
            every { mockAuthInfoRetrievePort.getAuthInfo(LoginProvider.GOOGLE, "registered") } returns authInfo
            `when`("다른 방법으로 가입되어 있다면") {
                every {
                    authInfo.email?.let {
                        mockUserAuthManagementPort.getUserAuthByEmail(it)
                    }
                } returns UserAuth(
                    userId = DomainId.generate(),
                    socialId = "socialId",
                    loginProvider = LoginProvider.APPLE,
                )
                then("UserException.UserAlreadyRegisteredException 예외가 발생한다.") {
                    shouldThrow<UserException.UserAuthAlreadyExistsException> {
                        socialLoginService.socialLogin(command)
                    }

                }
            }
            every {
                mockUserAuthManagementPort.getUserAuth(
                    authInfo.socialId,
                    authInfo.loginProvider,
                )
            } returns getUserAuth
            every { mockUserManagementPort.getUser(any()) } returns getUser
            every { mockUserTokenConvertPort.generateAccessToken(getUser) } returns "accessToken"
            every { mockUserTokenConvertPort.generateRefreshToken(getUser) } returns "refreshToken"
            `when`("기존에 존재한다면") {
                val response = socialLoginService.socialLogin(command)
                then("access token과 refresh token을 반환하는 success 인스턴스를 반환한다.") {
                    response.shouldBeInstanceOf<LoginUsecase.Success>()
                    response.accessToken.isNotEmpty() shouldBe true
                    response.refreshToken.isNotEmpty() shouldBe true
                    verify { mockUserTokenManagementPort.saveUserToken(any()) }
                }
            }

            every { mockUserManagementPort.getUser(any()) } returns null
            `when`("인증 정보만 존재하면 존재하고 사용자 정보가 없다면") {

                then("InvalidStateException 예외가 발생한다.") {
                    shouldThrow<DefaultException.InvalidStateException> {
                        socialLoginService.socialLogin(command)
                    }
                }
            }

            command = LoginUsecase.Command.Social(LoginProvider.GOOGLE.name, "nonRegistered")
            every { mockAuthInfoRetrievePort.getAuthInfo(LoginProvider.GOOGLE, "nonRegistered") } returns authInfo
            every {
                mockUserAuthManagementPort.getUserAuth(
                    authInfo.socialId,
                    authInfo.loginProvider,
                )
            } returns null
            every {
                authInfo.email?.let {
                    mockUserTokenConvertPort.generateRegisterToken(
                        socialId = authInfo.socialId,
                        socialLoginProvider = authInfo.loginProvider.name,
                        email = it
                    )
                }
            } returns "registerToken"
            `when`("가입되지 않았다면") {
                val response = socialLoginService.socialLogin(command)
                then("register token을 반환하는 nonRegistered 인스턴스를 반환한다.") {
                    response.shouldBeInstanceOf<LoginUsecase.NonRegistered>()
                    response.registerToken.isNotEmpty() shouldBe true
                    verify { mockUserTokenManagementPort.saveUserToken(any()) }
                }
            }
        }

        given("기본 로그인에서 요청한 사용자가") {
            val command = LoginUsecase.Command.Basic("email", "password")
            val getUser = UserFixture.createUser()
            val getUserAuth = UserAuth(
                userId = getUser.id,
                loginProvider = LoginProvider.BASIC,
                passwordHash = "password",
            )
            every { mockUserManagementPort.getUserNotNullByEmail(command.email) } returns getUser
            every { mockUserManagementPort.getUser(getUserAuth.userId) } returns getUser
            every { mockUserAuthManagementPort.getNotNull(getUser.id) } returns getUserAuth
            every { mockUserPasswordConvertPort.verifyPassword("password", getUserAuth.passwordHash) } returns true
            every { mockUserTokenConvertPort.generateAccessToken(getUser) } returns "accessToken"
            every { mockUserTokenConvertPort.generateRefreshToken(getUser) } returns "refreshToken"
            `when`("기존에 존재한다면") {
                val response = socialLoginService.basicLogin(command)
                then("access token과 refresh token을 반환하는 success 인스턴스를 반환한다.") {
                    response.shouldBeInstanceOf<LoginUsecase.Success>()
                    response.accessToken.isNotEmpty() shouldBe true
                    response.refreshToken.isNotEmpty() shouldBe true
                    verify { mockUserTokenManagementPort.saveUserToken(any()) }
                }
            }

            every { mockUserPasswordConvertPort.verifyPassword("password", getUserAuth.passwordHash) } returns false
            `when`("비밀번호가 일치하지 않는다면") {
                then("InvalidStateException 예외가 발생한다.") {
                    shouldThrow<DefaultException.InvalidStateException> {
                        socialLoginService.basicLogin(command)
                    }
                }
            }
        }
    })

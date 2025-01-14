package com.clip.application.user.service


import com.clip.application.user.exception.UserException
import com.clip.application.user.port.`in`.DeleteUserUsecase
import com.clip.application.user.port.`in`.RegisterUserUsecase
import com.clip.application.user.port.`in`.VerifyUserUsecase
import com.clip.application.user.port.out.*
import com.clip.application.user.vo.AuthInfo
import com.clip.application.user.vo.UserClaims
import com.clip.domain.UserFixture
import com.clip.domain.common.DomainId
import com.clip.domain.user.entity.User
import com.clip.domain.user.entity.UserAuth
import com.clip.domain.user.enums.LoginProvider
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserCommandServiceTest :
    BehaviorSpec({

        val mockUserManagementPort = mockk<UserManagementPort>(relaxed = true)
        val mockUserAuthManagementPort = mockk<UserAuthManagementPort>(relaxed = true)
        val mockUserTokenConvertPort = mockk<UserTokenConvertPort>()
        val mockUserTokenManagementPort = mockk<UserTokenManagementPort>(relaxed = true)
        val mockUserPasswordConvertPort = mockk<UserPasswordConvertPort>()

        val userCommandService =
            UserCommandService(
                mockUserTokenConvertPort,
                mockUserAuthManagementPort,
                mockUserManagementPort,
                mockUserTokenManagementPort,
                mockUserPasswordConvertPort
            )

        given("소셜 로그인을 통한 회원 가입 요청이 들어왔을 때") {
            val successCommand =
                RegisterUserUsecase.Command.Social(
                    "valid",
                    "nickname"
                )
            every { mockUserTokenManagementPort.isExistsToken("valid") } returns true
            every { mockUserTokenConvertPort.resolveRegisterToken("valid") } returns
                UserClaims.Register(
                    socialId = "123",
                    loginProvider = LoginProvider.GOOGLE,
                    email = "email",
                )
            every { mockUserAuthManagementPort.isExistsUserAuth("123", LoginProvider.GOOGLE) } returns false
            every { mockUserTokenConvertPort.generateAccessToken(any()) } returns "accessToken"
            every { mockUserTokenConvertPort.generateRefreshToken(any()) } returns "refreshToken"
            `when`("회원 가입이 성공하면") {
                val response = userCommandService.registerSocialUser(successCommand)
                then("access token과 refresh token을 반환한다.") {
                    response.accessToken.isNotEmpty() shouldBe true
                    response.refreshToken.isNotEmpty() shouldBe true
                    verify { mockUserManagementPort.saveUser(any()) }
                }
            }

            every { mockUserTokenConvertPort.resolveRegisterToken("duplicate") } returns
                UserClaims.Register(
                    socialId = "duplicate",
                    loginProvider = LoginProvider.GOOGLE,
                    email = "email"
                )
            every { mockUserTokenManagementPort.isExistsToken("duplicate") } returns true
            every { mockUserAuthManagementPort.isExistsUserAuth("duplicate", LoginProvider.GOOGLE) } returns true
            `when`("중복 가입 요청이 들어왔을 때") {
                val failCommand =
                    RegisterUserUsecase.Command.Social(
                        "duplicate",
                        "nickname"
                    )
                then("UserAlreadyRegisteredException 예외가 발생한다.") {
                    shouldThrow<UserException.UserAlreadyRegisteredException> {
                        userCommandService.registerSocialUser(failCommand)
                    }
                }
            }

            every { mockUserTokenManagementPort.isExistsToken("invalid") } returns true
            every { mockUserTokenConvertPort.resolveRegisterToken("invalid") } throws IllegalArgumentException("Invalid token")
            `when`("register token이 유요하지 않다면") {
                val failCommandWithoutRegisterToken =
                    RegisterUserUsecase.Command.Social("invalid", "nickname")
                then("예외가 발생한다.") {
                    shouldThrow<Exception> {
                        userCommandService.registerSocialUser(failCommandWithoutRegisterToken)
                    }
                }
            }

            every { mockUserTokenManagementPort.isExistsToken("non-saved") } returns false
            `when`("register token이 존재하지 않는다면") {
                val failCommandWithoutRegisterToken =
                    RegisterUserUsecase.Command.Social("non-saved", "nickname")
                then("UserPermissionDeniedException 예외가 발생한다.") {
                    shouldThrow<UserException.UserPermissionDeniedException> {
                        userCommandService.registerSocialUser(failCommandWithoutRegisterToken)
                    }
                }
            }

            every { mockUserTokenConvertPort.resolveRegisterToken("valid") } returns
                UserClaims.Register(
                    socialId = "123",
                    loginProvider = LoginProvider.GOOGLE,
                    email = "email",
                )
            every { mockUserTokenManagementPort.isExistsToken("valid") } returns true
            every { mockUserAuthManagementPort.isExistsUserAuth("123", LoginProvider.GOOGLE) } returns false

        }

        given("기본 로그인을 통한 회원 가입 요청이 들어왔을 때") {
            val successCommand =
                RegisterUserUsecase.Command.Basic(
                    "nickname",
                    "email",
                    "password"
                )
            every { mockUserPasswordConvertPort.hashPassword("password") } returns "hashedPassword"
            every { mockUserTokenConvertPort.generateAccessToken(any()) } returns "accessToken"
            every { mockUserTokenConvertPort.generateRefreshToken(any()) } returns "refreshToken"
            `when`("회원 가입이 성공하면") {
                val response = userCommandService.registerBasicUser(successCommand)
                then("access token과 refresh token을 반환한다.") {
                    response.accessToken.isNotEmpty() shouldBe true
                    response.refreshToken.isNotEmpty() shouldBe true
                    verify { mockUserManagementPort.saveUser(any()) }
                }
            }
        }

        given("회원 가입 검증 요청이 들어왔을 때") {
            val command = VerifyUserUsecase.Command("email")
            every { mockUserAuthManagementPort.getUserAuthByEmail("email") } returns null
            `when`("가입되지 않은 사용자일 경우") {
                val response = userCommandService.verifyUser(command)
                then("false를 반환한다.") {
                    response shouldBe VerifyUserUsecase.Response(false)
                }
            }

            val getUserAuth = UserAuth(
                userId = DomainId.generate(),
                loginProvider = LoginProvider.BASIC,
                passwordHash = "password",
            )
            every { mockUserAuthManagementPort.getUserAuthByEmail("email") } returns getUserAuth
            `when`("일반 로그인으로 가입한 사용자일 경우") {
                val response = userCommandService.verifyUser(command)
                then("true를 반환한다.") {
                    response shouldBe VerifyUserUsecase.Response(true)
                }
            }

            val socialUserAuth = UserAuth(
                userId = DomainId.generate(),
                socialId = "socialId",
                loginProvider = LoginProvider.GOOGLE,
            )
            every { mockUserAuthManagementPort.getUserAuthByEmail("email") } returns socialUserAuth
            `when`("소셜 로그인으로 가입한 사용자일 경우") {
                then("UserException.UserAlreadyRegisteredException 예외가 발생한다.") {
                    shouldThrow<UserException.UserAuthAlreadyExistsException> {
                        userCommandService.verifyUser(command)
                    }
                }
            }
        }

        given("회원 탈퇴 요청이 들어왔을 때") {
            val command = DeleteUserUsecase.Command("userId")
            val getUserAuth =
                UserAuth(
                    userId = DomainId.generate(),
                    socialId = "socialId",
                    loginProvider = LoginProvider.GOOGLE,
                )
            val getUser = UserFixture.createUser(getUserAuth.userId)
            every { mockUserManagementPort.getUserNotNull(DomainId(command.userId)) } returns getUser
            every { mockUserAuthManagementPort.getNotNull(getUser.id) } returns getUserAuth

            `when`("회원 탈퇴가 성공하면") {
                userCommandService.delete(command)
                then("회원 정보와 인증 정보를 삭제한다.") {
                    verify { mockUserManagementPort.delete(getUser) }
                    verify { mockUserAuthManagementPort.delete(getUserAuth) }
                }
            }
        }
    })

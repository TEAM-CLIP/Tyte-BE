package com.clip.bootstrap.user.controller

import com.clip.application.user.port.`in`.DeleteUserUsecase
import com.clip.application.user.port.`in`.LogoutUsecase
import com.clip.application.user.port.`in`.RegisterUserUsecase
import com.clip.application.user.port.`in`.VerifyUserUsecase
import com.clip.bootstrap.user.api.UserApi
import com.clip.bootstrap.user.dto.*
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val registerUserUsecase: RegisterUserUsecase,
    private val verifyUserUsecase: VerifyUserUsecase,
    private val logoutUsecase: LogoutUsecase,
    private val deleteUserUsecase: DeleteUserUsecase,
) : UserApi {

    override fun registerSocialUser(request: RegisterUserRequest.Social): RegisterUserResponse {
        val response = registerUserUsecase.registerSocialUser(
            RegisterUserUsecase.Command.Social(
                request.registerToken,
                request.nickname
            )
        )
        return RegisterUserResponse(response.accessToken, response.refreshToken)
    }

    override fun registerUser(request: RegisterUserRequest.Basic): RegisterUserResponse {
        val response = registerUserUsecase.registerBasicUser(
            RegisterUserUsecase.Command.Basic(
                request.email,
                request.nickname,
                request.password
            )
        )
        return RegisterUserResponse(response.accessToken, response.refreshToken)
    }

    override fun verifyUser(request: UserVerifyRequest): UserVerifyResponse {
        val response = verifyUserUsecase.verifyUser(
            VerifyUserUsecase.Command(
                request.email
            )
        )
        return UserVerifyResponse(response.isDuplicated)
    }

    override fun logout(
        userId: String,
        request: LogoutRequest,
    ) {
        logoutUsecase.logout(
            LogoutUsecase.Command(
                userId = userId,
                refreshToken = request.refreshToken,
            ),
        )
    }

    override fun deleteUser(userId: String) {
        deleteUserUsecase.delete(
            DeleteUserUsecase.Command(
                userId = userId,
            ),
        )
    }
}
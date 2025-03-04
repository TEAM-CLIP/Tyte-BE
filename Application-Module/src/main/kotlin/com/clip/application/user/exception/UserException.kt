package com.clip.application.user.exception

import com.clip.common.exception.BusinessException

sealed class UserException(
    codePrefix: String = CODE_PREFIX,
    errorCode: Int,
    httpStatus: Int = 400,
    message: String = DEFAULT_ERROR_MESSAGE
): BusinessException(codePrefix, errorCode, httpStatus, message) {

    class UserAlreadyRegisteredException: UserException(
        errorCode = 1,
        message = "이미 가입된 사용자입니다."
    )


    class UserAuthNotFoundException(
        message: String = "사용자 인증 정보를 찾을 수 없습니다."
    ): UserException(
        errorCode = 2,
        message = message
    )

    class UserNotFoundException: UserException(
        errorCode = 3,
        message = "사용자를 찾을 수 없습니다."
    )

    class UserPermissionDeniedException(
        message: String = "사용자 권한이 없습니다."
    ): UserException(
        errorCode = 4,
        message = message,
        httpStatus = 401
    )

    class UserAuthAlreadyExistsException: UserException(
        errorCode = 5,
        message = "이미 다른 방법으로 가입된 사용자입니다."
    )

    class UserTokenNotFoundException :
        UserException(
            errorCode = 6,
            message = "사용자 토큰을 찾을 수 없습니다.",
            httpStatus = 401,
        )

    class FriendNotFoundException: UserException(
        errorCode = 7,
        message = "친구를 찾을 수 없습니다."
    )

    class FriendRequestNotFoundException: UserException(
        errorCode = 8,
        message = "친구 요청을 찾을 수 없습니다."
    )

    class FriendAlreadyExistsException: UserException(
        errorCode = 9,
        message = "이미 친구 관계인 사용자입니다."
    )

    class FriendRequestAlreadyExistsException: UserException(
        errorCode = 10,
        message = "이미 친구 요청을 보낸 사용자입니다."
    )


    companion object{
        const val CODE_PREFIX = "USER"
        const val DEFAULT_ERROR_MESSAGE = "사용자와 관련된 예외가 발생했습니다."
    }
}
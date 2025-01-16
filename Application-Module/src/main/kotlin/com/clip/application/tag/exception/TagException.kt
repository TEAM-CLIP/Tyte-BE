package com.clip.application.tag.exception

import com.clip.common.exception.BusinessException

sealed class TagException(
    codePrefix: String = CODE_PREFIX,
    errorCode: Int,
    httpStatus: Int = 400,
    message: String = DEFAULT_ERROR_MESSAGE
) : BusinessException(codePrefix, errorCode, httpStatus, message) {
    class TagNotFoundException(
        message: String = "해당 태그를 찾을 수 없습니다."
    ) : TagException(
        errorCode = 1,
        message = message
    )

    class TagAlreadyExistsException(
        message: String = "이미 존재하는 태그입니다."
    ) : TagException(
        errorCode = 2,
        message = message
    )

    companion object {
        const val CODE_PREFIX = "TAG"
        const val DEFAULT_ERROR_MESSAGE = "태그와 관련된 예외가 발생했습니다."
    }
}
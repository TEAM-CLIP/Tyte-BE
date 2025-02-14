package com.clip.application.todo.exception

import com.clip.common.exception.BusinessException

sealed class TodoException (
    codePrefix: String = CODE_PREFIX,
    errorCode: Int,
    httpStatus: Int = 400,
    message: String = DEFAULT_ERROR_MESSAGE
) : BusinessException(codePrefix, errorCode, httpStatus, message) {

    class TodoNotFoundException(
        message: String = "해당 투두를 찾을 수 없습니다."
    ) : TodoException(
        errorCode = 1,
        message = message
    )

    companion object {
        const val CODE_PREFIX = "Todo"
        const val DEFAULT_ERROR_MESSAGE = "투두와 관련된 예외가 발생했습니다."
    }
}

package com.clip.client.openai.exception

import com.clip.common.exception.BusinessException

sealed class OpenAIException(
    codePrefix: String = CODE_PREFIX,
    errorCode: Int,
    httpStatus: Int = 400,
    message: String = DEFAULT_ERROR_MESSAGE
) : BusinessException(codePrefix, errorCode, httpStatus, message) {

    class OpenAIRetrieveFailedException(
        message: String = "OpenAI 정보를 가져오는데 실패했습니다."
    ) : OpenAIException(
        errorCode = 1,
        message = message
    )

    companion object {
        const val CODE_PREFIX = "OPENAI"
        const val DEFAULT_ERROR_MESSAGE = "OpenAI 관련 예외가 발생했습니다."
    }

}
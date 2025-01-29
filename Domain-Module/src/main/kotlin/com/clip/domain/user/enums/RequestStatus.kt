package com.clip.domain.user.enums

enum class RequestStatus {
    PENDING,
    ACCEPTED,
    REJECTED
    ;

    companion object {
        fun parse(value: String): RequestStatus {
            return entries.firstOrNull { it.name == value }
                ?: throw IllegalArgumentException("유효하지 않은 요청 상태입니다.")
        }
    }
}
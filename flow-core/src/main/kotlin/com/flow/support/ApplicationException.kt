package com.flow.support

class ApplicationException(
    val errorCode: ErrorCode,
    val customMessage: String? = null,
) : RuntimeException(customMessage ?: errorCode.message) {
}
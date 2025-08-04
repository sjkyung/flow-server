package com.website


data class ApiResponse<T>(
    val meta: Metadata,
    val data: T?
)

data class Metadata(
    val success: Boolean,
    val message: String?
)

data class AllowedUserResponse(
    val allowed: Boolean,
)

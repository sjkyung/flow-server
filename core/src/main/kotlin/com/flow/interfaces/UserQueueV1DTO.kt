package com.flow.interfaces

class UserQueueV1DTO {

    data class RegisterUserResponse(
        val rank: Long,
    ) {
        companion object {
            fun from(rank: Long): RegisterUserResponse {
                return RegisterUserResponse(
                    rank = rank
                )
            }
        }
    }

    data class AllowUserResponse(
        val requestCount: Long,
        val allowCount: Long,
    ) {
        companion object {
            fun from(requestCount: Long,allowCount: Long): AllowUserResponse {
                return AllowUserResponse(
                    requestCount = requestCount,
                    allowCount = allowCount
                )
            }
        }
    }

    data class AllowedUserResponse(
        val allowed: Boolean,
    ) {
        companion object {
            fun from(allowed: Boolean): AllowedUserResponse {
                return AllowedUserResponse(
                    allowed = allowed,
                )
            }
        }
    }

    data class RankNumberResponse(
        val rank: Long,
    ) {
        companion object {
            fun from(rank: Long): RankNumberResponse {
                return RankNumberResponse(
                    rank = rank
                )
            }
        }
    }
}

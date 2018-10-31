package com.levinzonr.ezpad.domain.responses

data class UserResponse(
        val uuid: Long,
        val email: String,
        val fistName: String?,
        val lastName: String?,
        val displayName: String?,
        val photoUrl: String?
)
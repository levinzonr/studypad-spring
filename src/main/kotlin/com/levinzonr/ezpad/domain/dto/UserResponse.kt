package com.levinzonr.ezpad.domain.dto

data class UserResponse(
        val uuid: String,
        val email: String,
        val fistName: String?,
        val lastName: String?,
        val displayName: String?,
        val photoUrl: String?
)
package com.levinzonr.ezpad.domain.dto

data class UserDto(
        val uuid: String,
        val email: String,
        val fistName: String?,
        val lastName: String?,
        val displayName: String?,
        val photoUrl: String?
)
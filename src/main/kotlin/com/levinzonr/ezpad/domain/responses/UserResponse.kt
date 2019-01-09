package com.levinzonr.ezpad.domain.responses


data class UserResponse(
        val uuid: Long,
        val email: String,
        val firstName: String?,
        val lastName: String?,
        val displayName: String?,
        val university: UniversityResponse?,
        val photoUrl: String?,
        val isNewUser: Boolean = false
)

data class AuthorResponse(
        val uuid: Long,
        val displayName: String?,
        val photoUrl: String?,
        val university: UniversityResponse?
)
package com.levinzonr.ezpad.domain.responses


data class UserResponse(
        val uuid: String,
        val email: String,
        val firstName: String?,
        val lastName: String?,
        val displayName: String?,
        val university: UniversityResponse?,
        val photoUrl: String?,
        val isNewUser: Boolean = false
)

data class AuthorResponse(
        val uuid: String,
        val displayName: String?,
        val photoUrl: String?,
        val university: UniversityResponse?
)


data class FirebaseLoginResponse(
        val userResponse: UserResponse,
        val isNewUser: Boolean
)


data class FirebaseTokenResponse(
        val token: String,
        val user: UserResponse
)
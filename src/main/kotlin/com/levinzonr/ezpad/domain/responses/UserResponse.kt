package com.levinzonr.ezpad.domain.responses

import com.levinzonr.ezpad.domain.model.University

data class UserResponse(
        val uuid: Long,
        val email: String,
        val fistName: String?,
        val lastName: String?,
        val displayName: String?,
        val university: UniversityResponse?,
        val photoUrl: String?
)
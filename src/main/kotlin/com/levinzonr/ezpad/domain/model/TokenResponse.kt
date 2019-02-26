package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.UserResponse

class TokenResponse(
        val access_token: String,
        val token_type: String = "",
        val refresh_token: String = "",
        val expires_in: Int = 12,
        var user: UserResponse? = null
)
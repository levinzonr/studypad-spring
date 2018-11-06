package com.levinzonr.ezpad.domain.model

class TokenResponse(
        val access_token: String,
        val token_type: String,
        val refresh_token: String,
        val expires_in: Int,
        val scope: String
)
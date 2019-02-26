package com.levinzonr.ezpad.domain.payload

data class FirebaseLoginPayload(
        val token: String,
        val firstName: String? = null,
        val lastName: String? = null
)
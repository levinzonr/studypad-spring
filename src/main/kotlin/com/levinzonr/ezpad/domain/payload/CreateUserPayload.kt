package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.Email
import javax.validation.constraints.Size
import kotlin.math.min

data class CreateUserPayload(

        @Email
        val email: String,

        @Size(min = 6, max = 100)
        val password: String,

        val firstName: String?,

        val lastName: String?
)
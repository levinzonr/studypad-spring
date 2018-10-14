package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class CreateUserPayload(

        @field: NotEmpty
        @field: Email
        val email: String,


        @field: Size (min = 6, max = 100)
        val password: String,

        val firstName: String?,

        val lastName: String?
)
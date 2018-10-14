package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

data class CreateUserPayload(

        @field: NotEmpty
        @field: Email
        @field: NotNull
        val email: String?,


        @field: Size (min = 6, max = 100)
        @field: NotNull
        val password: String?,

        val firstName: String?,

        val lastName: String?
)
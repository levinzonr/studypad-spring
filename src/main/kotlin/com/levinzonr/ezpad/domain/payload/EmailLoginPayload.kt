package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class EmailLoginPayload(

        @field:Email
        @field:NotNull
        @field:NotEmpty
        val email: String,


        @field:NotNull
        @field:NotEmpty
        val password: String
)
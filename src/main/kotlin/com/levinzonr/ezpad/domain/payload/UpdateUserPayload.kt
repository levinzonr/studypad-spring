package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class UpdateUserPayload(

        val firstName: String?,
        val lastName: String?,

        @field: Size(min = 6, max =  100)
        val password: String?
)
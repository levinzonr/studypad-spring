package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

data class UpdateUserPayload(

        val universityId: Long?,

        @field:NotEmpty
        val displayName: String?
)
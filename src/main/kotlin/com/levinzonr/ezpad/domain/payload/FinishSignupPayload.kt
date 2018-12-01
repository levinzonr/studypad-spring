package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class FinishSignupPayload(

        @field: NotNull
        val universityId: Long?

)
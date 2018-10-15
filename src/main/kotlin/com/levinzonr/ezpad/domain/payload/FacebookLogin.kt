package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class FacebookLogin (

        @field: NotEmpty
        @field: NotNull
        val token:  String?
)
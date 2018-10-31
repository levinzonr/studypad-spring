package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class CreateNotebookPayload(

        @field:NotEmpty
        @field:NotNull
        val name: String,

        @field:NotEmpty
        @field:NotNull
        val color: String
)
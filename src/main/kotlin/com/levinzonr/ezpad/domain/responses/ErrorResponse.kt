package com.levinzonr.ezpad.domain.responses

data class ErrorResponse(
        val type: String,
        val message: String,
        val errors: List<FieldError>? = null
)
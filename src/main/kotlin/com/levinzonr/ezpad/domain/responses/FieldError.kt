package com.levinzonr.ezpad.domain.responses

data class FieldError(
        val fieldName: String,
        val error: String
)
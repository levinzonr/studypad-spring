package com.levinzonr.ezpad.domain.errors

import com.levinzonr.ezpad.domain.ApiMessages
import com.levinzonr.ezpad.domain.dto.ErrorResponse
import com.levinzonr.ezpad.domain.dto.FieldError

class InvalidPayloadException(private val errors: List<FieldError>? = null) : EzpadException("") {


    override fun toResponse(): ErrorResponse {
        return ErrorResponse(
                type = ApiMessages.ErrorTypes.TYPE_BAD_REQUEST_BODY,
                message = ApiMessages.ErrorMessages.ERROR_INVALID_PAYLOAD,
                errors = errors
        )
    }
}
package com.levinzonr.ezpad.domain.errors

import com.levinzonr.ezpad.domain.ApiMessages
import com.levinzonr.ezpad.domain.responses.ErrorResponse

class BadRequestException(message: String) : EzpadException(message) {
    override fun toResponse(): ErrorResponse {
        return ErrorResponse(
                ApiMessages.ErrorTypes.TYPE_BAD_REQUEST,
                message
        )
    }
}
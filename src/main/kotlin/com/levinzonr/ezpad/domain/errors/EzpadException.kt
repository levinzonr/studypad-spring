package com.levinzonr.ezpad.domain.errors

import com.levinzonr.ezpad.domain.dto.ErrorResponse

abstract class EzpadException(override val message: String = "") : Exception(message) {

    abstract fun toResponse() : ErrorResponse
}
package com.levinzonr.ezpad.controllers

import com.google.firebase.auth.FirebaseAuthException
import com.levinzonr.ezpad.domain.ApiMessages
import com.levinzonr.ezpad.domain.responses.ErrorResponse
import com.levinzonr.ezpad.domain.responses.FieldError
import com.levinzonr.ezpad.domain.errors.BadRequestException
import com.levinzonr.ezpad.domain.errors.NotFoundException
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class RestControllerExceptionHandler {


    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    fun handle(excetption: Exception) : ErrorResponse {
        return ErrorResponse(
                type = ApiMessages.ErrorTypes.TYPE_UNKNOWN,
                message = ApiMessages.ErrorMessages.ERROR_UNKNOWN + ": " + excetption.localizedMessage
        )
    }



    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(notFoundException: NotFoundException) : ErrorResponse {
        return notFoundException.toResponse()
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ResponseBody
    fun handleTokenExpired(authException: FirebaseAuthException) : ErrorResponse {
        return ErrorResponse(type = "auth_error", message = "Token expired")
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadPayload(exception: MethodArgumentNotValidException) : ErrorResponse {
        val errors = exception.bindingResult.fieldErrors.map { FieldError(it.field, it.defaultMessage ?: ApiMessages.ErrorMessages.ERROR_UNKNOWN) }
        return ErrorResponse(ApiMessages.ErrorTypes.TYPE_BAD_REQUEST_BODY, ApiMessages.ErrorMessages.ERROR_INVALID_PAYLOAD, errors)
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleBadPayload(badRequestException: BadRequestException) : ErrorResponse {
        return badRequestException.toResponse()
    }





}
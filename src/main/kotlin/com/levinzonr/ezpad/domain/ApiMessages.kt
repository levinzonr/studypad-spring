package com.levinzonr.ezpad.domain

object ApiMessages {

    object ErrorTypes {
        const val TYPE_BAD_REQUEST_BODY = "error_bad_request_body"
        const val TYPE_BAD_REQUEST = "error_bad_request"
        const val TYPE_REPOSITORY_ERROR = "error_repository"
        const val TYPE_UNKNOWN = "error_unknown"
        const val TYPE_NOT_FOUND  = "error_not_found"
    }

    object ErrorMessages {
        const val ERROR_INVALID_PAYLOAD = "Some of the fields are either incorrect or missing"
        const val ERROR_MISSING_FIELD = "Field is either blank or missing"
        const val ERROR_USER_EXISTS = "User with this email already exists"
        const val ERROR_UNKNOWN = "Something bad has happened :("
        const val ERROR_USER_NOT_FOUND = "No such user exists with id [id]"
        const val ERROR_NOTEBOOK_NOT_FOUND = "No notebook by id [id] exists"
        const val ERROR_NOTE_NOT_FOUND = "No note with id [id] found"
        const val ERROR_NOT_FOUND = "Don't have anything like that, sorry :D"
        const val ERROR_UNI_NOT_FOUND = "No university exists by id [id]"
    }

}
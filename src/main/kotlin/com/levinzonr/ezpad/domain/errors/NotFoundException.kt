package com.levinzonr.ezpad.domain.errors

import com.levinzonr.ezpad.domain.ApiMessages
import com.levinzonr.ezpad.domain.responses.ErrorResponse
import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.University
import com.levinzonr.ezpad.domain.model.User
import kotlin.reflect.KClass

class NotFoundException(message: String) : EzpadException(message) {


    override fun toResponse(): ErrorResponse {
        return ErrorResponse(
                type = ApiMessages.ErrorTypes.TYPE_NOT_FOUND,
                message = message
        )
    }

    companion object {
        inline fun <reified T: Any> buildWithId(id: String) : NotFoundException {
            return Builder(T::class).buildWithId(id)
        }
    }

    class  Builder<T : Any>(classValue: KClass<T>) {

        private val message = when (classValue) {
            User::class -> {
                ApiMessages.ErrorMessages.ERROR_USER_NOT_FOUND
            }
            Notebook::class -> {
                ApiMessages.ErrorMessages.ERROR_NOTEBOOK_NOT_FOUND
            }
            Note::class -> {
                ApiMessages.ErrorMessages.ERROR_NOTE_NOT_FOUND
            }

            University::class -> {
                ApiMessages.ErrorMessages.ERROR_UNI_NOT_FOUND
            }

            else -> {
                ApiMessages.ErrorMessages.ERROR_NOT_FOUND.replace("[entity]", classValue.simpleName.toString())
            }
        }

        fun buildWithId(id: String) : NotFoundException {
            return NotFoundException(message.replace("[id]", id))
        }

    }

}
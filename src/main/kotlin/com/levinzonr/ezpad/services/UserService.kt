package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.User
import java.util.*

interface UserService {

    fun createUser(
            email: String, password: String,
            firstName: String? = null, lastName: String? = null,
            photoUrl: String? = null ) : User


    fun getUserById(uuid: UUID) : User

    fun updateUserById(uuid: String, firstName: String?,
                       lastName: String?, password: String?) : User


}
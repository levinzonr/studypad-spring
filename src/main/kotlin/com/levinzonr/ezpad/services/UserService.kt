package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.User
import java.util.*

interface UserService {

    fun createUser(
            email: String, password: String,
            firstName: String?, lastName: String?,
            photoUrl: String? ) : User


    fun getUserById(uuid: UUID) : User

}
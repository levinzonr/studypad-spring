package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.model.UserRole
import com.levinzonr.ezpad.domain.payload.FacebookUser
import java.util.*

interface UserService {

    fun createUser(
            email: String, password: String,
            firstName: String? = null, lastName: String? = null,
            photoUrl: String? = null, role: UserRole = UserRole.USER) : User


    fun getUserById(id: Long) : User

    fun getUserEmail(email: String) : User

    fun updateUserById(id: Long, firstName: String?,
                       lastName: String?, password: String?) : User

    fun processFacebookUser(facebookUser: FacebookUser) : User


}
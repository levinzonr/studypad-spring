package com.levinzonr.ezpad.services

import com.google.firebase.auth.UserRecord
import com.levinzonr.ezpad.domain.model.University
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.model.UserRole
import com.levinzonr.ezpad.domain.payload.FacebookUser
import java.util.*

interface UserService {

    /**
     * Creates firebase user, saves him to the database
     */
    fun createUser(email: String, password: String, firstName: String?, lastName: String?, locale: String) : User

    fun createUser(firebaseId: String, locale: String) : User

    /**
     * Returns a user from the database
     */
    fun findUserById(id: String) : User?


    fun updateUser(userId: String, displayName: String?, universityId: Long? = null, locale: String? = null) : User

    fun registerFirebaseToken(userId: String, token: String)

    fun unregisterFirebaseToken(userId: String, token: String)

    fun hasAdminPriveleges(userId: String) : Boolean
}
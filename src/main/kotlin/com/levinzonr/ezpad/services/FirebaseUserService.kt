package com.levinzonr.ezpad.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FirebaseUserService : UserService {


    @Autowired
    private lateinit var auth: FirebaseAuth

    @Autowired
    private lateinit var repository: UserRepository

    override fun createUser(email: String, password: String, firstName: String?, lastName: String?): User {
        val displayName = if ("${firstName ?: ""} ${lastName
                        ?: ""}".isBlank()) "Unknown user" else "$firstName $lastName"

        val request = UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(displayName)
                .setEmailVerified(false)


        val userRecord = auth.createUser(request)

        val user = User(userRecord.uid, userRecord.email, firstName, lastName, userRecord.displayName, userRecord.photoUrl)
        return repository.save(user)
    }

    override fun createUser(firebaseId: String): User {
        val userRecord = auth.getUser(firebaseId)
        val dbUser = User(userRecord.uid, userRecord.email, null, null, userRecord.displayName, userRecord.photoUrl)
        return repository.save(dbUser)
    }

    override fun findUserById(id: String): User? {
        return repository.findById(id).get()
    }

}
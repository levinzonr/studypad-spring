package com.levinzonr.ezpad.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.model.UserRole
import com.levinzonr.ezpad.domain.payload.FacebookUser
import com.levinzonr.ezpad.domain.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.lang.IllegalArgumentException

@Service
class FirebaseUserService : UserService {

    @Autowired
    private lateinit var repository: UserRepository

    private val auth : FirebaseAuth
        get() = FirebaseAuth.getInstance()

    override fun createUser(email: String, password: String, firstName: String?, lastName: String?, photoUrl: String?, role: UserRole): User {
        val auth = FirebaseAuth.getInstance()
        val userRequest = UserRecord.CreateRequest().apply {
            setEmail(email)
            setEmailVerified(false)
            setPassword(password)
            setDisplayName(firstName)
        }

        val createdUser = auth.createUser(userRequest)
        return processUserRecord(createdUser)
    }


    override fun getUserById(id: String): User {
        val user = repository.findById(id)
        val firebaseUser = auth.getUser(id)
        return if (user.isPresent) user.get()
        else {
            processUserRecord(firebaseUser)
        }
    }

    override fun getUserEmail(email: String): User {
      throw  IllegalArgumentException("deprecetad")
    }

    override fun updateUserById(id: String, firstName: String?, lastName: String?, password: String?): User {
        val user = getUserById(id)
        val updated = user.copy(
                firstName = firstName ?: user.firstName,
                lastName = lastName ?: user.lastName,
                displayName = "$firstName $lastName",
                password = password
        )
        return repository.save(updated)
    }

    override fun processFacebookUser(facebookUser: FacebookUser): User {
        throw IllegalArgumentException("depreacted")
    }

    override fun updateUserUniversity(userId: String, universityId: Long): User {
        throw IllegalArgumentException("depreacted")
    }

    private fun processUserRecord(record: UserRecord) : User {
        val user = User(
                id = record.uid,
                email = record.email,
                firstName = record.displayName,
                photoUrl = record.photoUrl
        )
        return repository.save(user)
    }
}
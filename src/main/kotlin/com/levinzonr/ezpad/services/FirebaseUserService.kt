package com.levinzonr.ezpad.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserRecord
import com.levinzonr.ezpad.domain.errors.BadRequestException
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.UserRepository
import com.levinzonr.ezpad.utils.tryGet
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FirebaseUserService : UserService {


    @Autowired
    private lateinit var auth: FirebaseAuth

    @Autowired
    private lateinit var repository: UserRepository

    @Autowired
    private lateinit var universityService: UniversityService

    override fun createUser(email: String, password: String, firstName: String?, lastName: String?): User {
        val displayName = if ("${firstName ?: ""} ${lastName
                        ?: ""}".isBlank()) "Unknown user" else "$firstName $lastName"
        println("Create from firebase reqyest")

        val existedUser : UserRecord? = try { auth.getUserByEmail(email) } catch (e: Exception) { null }
        if (existedUser != null) {
            throw BadRequestException("User already exist")
        }


        val request = UserRecord.CreateRequest()
                .setEmail(email)
                .setPassword(password)
                .setDisplayName(displayName)
                .setEmailVerified(false)



        val userRecord = auth.createUserAsync(request).get()

        val user = User(userRecord.uid, userRecord.email, firstName, lastName, userRecord.displayName, userRecord.photoUrl)
        return repository.save(user)
    }

    override fun createUser(firebaseId: String): User {
        val userRecord = auth.getUser(firebaseId)
        println("Create from firebase recoed")
        val names = userRecord.displayName.split(" ")
        val firstName = names.getOrNull(0)
        val lastName = names.getOrNull(1)

        val dbUser = User(userRecord.uid, userRecord.email, firstName, lastName, userRecord.displayName, userRecord.photoUrl)
        return repository.save(dbUser).apply { isNewUser = true }
    }

    override fun findUserById(id: String): User? {
        val user = repository.findById(id).tryGet()
        println("Found user: ${user?.id}")
        return user
    }

    override fun updateUser(userId: String, universityId: Long?) : User {
        var user = findUserById(userId) ?: throw NotFoundException.Builder(User::class).buildWithId(userId)
        universityId?.let { id ->
            val uni = universityService.findById(id)
            user = repository.save(user.copy(university = uni))
        }

        return user
    }
}
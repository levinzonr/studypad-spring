package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.ApiMessages
import com.levinzonr.ezpad.domain.errors.BadRequestException
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.model.UserRole
import com.levinzonr.ezpad.domain.payload.FacebookUser
import com.levinzonr.ezpad.domain.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var universityService: UniversityService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder


    override fun createUser(email: String, password: String,
                            firstName: String?, lastName: String?, photoUrl: String?, role: UserRole): User {

        userRepository.findByEmail(email)?.let {
            throw BadRequestException(ApiMessages.ErrorMessages.ERROR_USER_EXISTS)
        }


        val user = User(
                email = email,
                password = passwordEncoder.encode(password),
                firstName = firstName,
                lastName = lastName,
                displayName = "$firstName $lastName",
                photoUrl = photoUrl,
                roles = setOf(role)
        )

        return userRepository.save(user)
    }

    override fun getUserById(id: Long): User {
        return userRepository.findById(id)
                .orElseThrow {
                    NotFoundException.Builder(User::class)
                            .buildWithId(id.toString())
                }
    }

    override fun updateUserById(uuid: Long, firstName: String?, lastName: String?, password: String?): User {
        val user = getUserById(uuid)
        val updated = user.copy(
                firstName = firstName ?: user.firstName,
                lastName = lastName ?: user.lastName,
                displayName = "$firstName $lastName",
                password = password
        )
        return userRepository.save(updated)
    }

    // TODO BBetter password handling
    override fun processFacebookUser(facebookUser: FacebookUser): User {
        // Facebook User doesn't exist
        return userRepository.findByEmail(facebookUser.email!!) ?:
        createUser(facebookUser.email, facebookUser.id!!, facebookUser.first_name, facebookUser.last_name, null, UserRole.FACEBOOK_USER)
    }

    override fun getUserEmail(email: String): User {
        return userRepository.findByEmail(email) ?: throw NotFoundException.Builder(User::class).buildWithId(email)
    }

    override fun updateUserUniversity(userId: Long, universityId: Long): User {
        println(universityService.findAll())
        val uni =  universityService.findById(universityId)
        val newUser = getUserById(userId).copy(university = uni)
        userRepository.save(newUser)
        return newUser
    }
}
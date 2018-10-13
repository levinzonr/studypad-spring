package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    override fun createUser(email: String, password: String,
                            firstName: String?, lastName: String?, photoUrl: String?): User? {

        userRepository.findByEmail(email)?.let {
            return null
        }

        val user = User(
                email = email,
                password = passwordEncoder.encode(password),
                firstName = firstName,
                lastName = lastName,
                displayName = "$firstName $lastName",
                photoUrl = photoUrl
        )

        return userRepository.save(user)
    }

    override fun getUserById(uuid: UUID?): Optional<User> {
        return if (uuid != null) userRepository.findById(uuid)
        else Optional.empty()
    }
}
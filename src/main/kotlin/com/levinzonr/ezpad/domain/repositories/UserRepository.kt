package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.User
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, Long> {

        fun findByEmail(email: String) : User?

}
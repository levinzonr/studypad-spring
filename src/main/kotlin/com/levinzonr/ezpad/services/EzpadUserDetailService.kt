package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.repositories.UserRepository
import com.levinzonr.ezpad.security.EzpadUserDetails
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class EzpadUserDetailService : UserDetailsService {

    @Autowired
    lateinit var userRepository: UserRepository

    override fun loadUserByUsername(username: String?): UserDetails {
       username?.let {
           val user = userRepository.findByEmail(it) ?: throw UsernameNotFoundException("No such user ($it)")
           return EzpadUserDetails(user)
       }
        throw UsernameNotFoundException("Name can't be null")
    }
}
package com.levinzonr.ezpad.security

import com.levinzonr.ezpad.domain.model.UserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User


class StudyPadUserDetails(val id: String, val email: String)
    : User(email, null, createAuthorities(setOf(UserRole.USER))) {

    val userId: String
        get() = id




    companion object {
        private const val PREFIX = "ROLE_"

        fun createAuthorities(roles: Set<UserRole>) : Collection<SimpleGrantedAuthority> {
            return roles.map { SimpleGrantedAuthority("$PREFIX${it.name}")}
        }

    }

}
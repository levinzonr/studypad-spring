package com.levinzonr.ezpad.security

import com.levinzonr.ezpad.domain.model.UserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import java.util.*
import javax.management.relation.Role


class EzpadUserDetails(user: com.levinzonr.ezpad.domain.model.User)
    : User(user.email, user.password, createAuthorities(user.roles)) {

    val userId: UUID? = user.id




    companion object {
        private const val PREFIX = "ROLE_"

        fun createAuthorities(roles: Set<UserRole>) : Collection<SimpleGrantedAuthority> {
            return roles.map { SimpleGrantedAuthority("$PREFIX${it.name}")}
        }

    }

}
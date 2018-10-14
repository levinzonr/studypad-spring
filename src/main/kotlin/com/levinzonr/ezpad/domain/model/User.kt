package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.dto.UserResponse
import java.util.*
import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue
        val id: UUID? = null,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null,
        val displayName: String? = null,
        val photoUrl: String? = null,
        val password: String? = null) {


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    val roles = setOf(UserRole.USER)


    fun toResponse(): UserResponse {
        return UserResponse(
                uuid = id.toString(),
                email = email,
                fistName = firstName,
                lastName = lastName,
                displayName = displayName,
                photoUrl = photoUrl
        )
    }
}
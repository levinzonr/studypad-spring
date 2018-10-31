package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.UserResponse
import javax.persistence.*

@Entity
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val email: String,
        val firstName: String? = null,
        val lastName: String? = null,
        val displayName: String? = null,
        val photoUrl: String? = null,

        @OneToMany(mappedBy = "user")
        val notebooks: List<Notebook> = listOf(),
        val password: String? = null) {


    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    val roles = setOf(UserRole.USER)


    fun toResponse(): UserResponse {
        return UserResponse(
                uuid = id!!,
                email = email,
                fistName = firstName,
                lastName = lastName,
                displayName = displayName,
                photoUrl = photoUrl
        )
    }
}
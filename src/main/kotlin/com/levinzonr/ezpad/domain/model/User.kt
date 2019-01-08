package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.AuthorResponse
import com.levinzonr.ezpad.domain.responses.UserResponse
import javax.persistence.*

@Entity(name = "studypad_user")
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

        @ManyToOne
        @JoinColumn(name = "university_id")
        val university: University? = null,

        val password: String? = null,

        @ElementCollection(fetch = FetchType.EAGER)
        @Enumerated(EnumType.STRING)
        val roles: Set<UserRole> = setOf(UserRole.USER)) {

        @Transient
        var isNewUser: Boolean = false

    fun toResponse(): UserResponse {
        return UserResponse(
                uuid = id!!,
                email = email,
                firstName = firstName,
                lastName = lastName,
                displayName = displayName,
                university = university?.toResponse(),
                photoUrl = photoUrl,
                isNewUser = isNewUser
        )
    }

    fun toAuthorResponse() : AuthorResponse {
        return AuthorResponse(id!!, displayName, photoUrl, university?.toResponse())
    }
}
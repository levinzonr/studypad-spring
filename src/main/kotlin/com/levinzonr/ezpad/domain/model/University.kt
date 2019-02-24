package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.UniversityResponse
import javax.persistence.*

@Entity
data class University(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val fullName: String,
        val shortName: String,

        @OneToMany(mappedBy = "university")
        val students: Set<User> = setOf(),

        val aliases: String = ""
) {

    fun aliases() : List<String> {
        return aliases.split(";")
    }

    fun aliasMatch(query: String): Boolean {
        return aliases().any { it.startsWith(query, true) }
    }


    fun toResponse() : UniversityResponse {
        return UniversityResponse(fullName, shortName, id!!)
    }

}




data class ExportedUniversity(
        val web_pages: List<String>,
        val name: String,
        val alpha_two_code: String,
        val state_province: String,
        val domains: List<String>,
        val country: String
)
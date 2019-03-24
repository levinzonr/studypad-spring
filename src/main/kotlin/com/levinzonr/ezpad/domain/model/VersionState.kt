package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.VersionStateResponse
import javax.persistence.*

@Entity
data class VersionState(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @OneToOne(mappedBy = "state")
        val notebook: BaseNotebook,

        @OneToMany(cascade = [CascadeType.ALL], mappedBy = "state")
        val modifications : List<Modification> = listOf(),

        val version: Int
)

fun VersionState.toResponse() : VersionStateResponse {
        return VersionStateResponse(version, modifications.map { it.toResponse() })
}
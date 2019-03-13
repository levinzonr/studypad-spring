package com.levinzonr.ezpad.domain.model

import javax.persistence.*

@Entity
data class VersionState(

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @OneToOne
        val notebook: BaseNotebook,

        @OneToMany(mappedBy = "state")
        val modifications : List<Modification> = listOf(),

        val version: Int
)
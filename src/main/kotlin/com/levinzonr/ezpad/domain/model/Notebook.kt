package com.levinzonr.ezpad.domain.model

import java.util.*
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Notebook(
        @Id
        @GeneratedValue
        val id: UUID? = null,

        val name: String? = null,
        val colour: String? = null
)
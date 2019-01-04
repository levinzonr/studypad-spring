package com.levinzonr.ezpad.domain.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id

@Entity
data class Topic(
        @GeneratedValue
        @Id
        val id: Long? = null,
        val name: String
)
package com.levinzonr.ezpad.domain

import java.util.*
import javax.persistence.GeneratedValue
import javax.persistence.Id

data class Note(
        @Id
        @GeneratedValue
        val id: UUID? = null,
        val title: String? = null,
        val content: String? = null
)
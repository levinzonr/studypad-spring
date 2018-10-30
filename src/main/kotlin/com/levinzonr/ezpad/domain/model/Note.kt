package com.levinzonr.ezpad.domain.model

import java.util.*
import javax.persistence.*

@Entity
data class Note(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val title: String? = null,
        val content: String? = null,

        @ManyToOne
        @JoinColumn(name = "notebook_id")
        val notebook: Notebook
)
package com.levinzonr.ezpad.domain.model

import javax.persistence.*

@Entity
data class Notebook(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val name: String,
        val colour: String? = null,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val user: User,

        @OneToMany(mappedBy = "notebook", cascade = [CascadeType.ALL])
        val notes: List<Note> = listOf()
)
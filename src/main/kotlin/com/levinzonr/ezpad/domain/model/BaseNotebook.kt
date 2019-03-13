package com.levinzonr.ezpad.domain.model

import java.util.*
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class BaseNotebook(

        @Id
        val id: String = UUID.randomUUID().toString(),

        @ManyToOne
        @JoinColumn(name = "user_id")
        val author: User,

        @OneToMany(mappedBy = "notebook", cascade = [CascadeType.ALL])
        val notes: List<Note> = listOf(),

        @OneToOne
        val state: VersionState? = null
)
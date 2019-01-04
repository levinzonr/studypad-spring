package com.levinzonr.ezpad.domain.model

import javax.persistence.*
import java.util.HashSet
import javax.persistence.JoinColumn
import javax.persistence.JoinTable



@Entity
data class Tag(
        @Id
        val name: String,

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "shared", joinColumns = [JoinColumn(name = "shared_id")], inverseJoinColumns = [JoinColumn(name = "tag_name")])
        val notebooks: List<PublishedNotebook> = listOf()
)
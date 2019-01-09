package com.levinzonr.ezpad.domain.model

import javax.persistence.*
import java.util.HashSet
import javax.persistence.JoinColumn
import javax.persistence.JoinTable


@Entity
data class Tag(
        @Id
        val name: String,

        @ManyToMany(fetch = FetchType.LAZY,
                cascade = [
                    CascadeType.PERSIST,
                    CascadeType.MERGE
                ],
                mappedBy = "tags")
        val notebooks: List<PublishedNotebook> = listOf()
)
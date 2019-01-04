package com.levinzonr.ezpad.domain.model

import java.util.*
import javax.persistence.*


@Entity(name = "shared")
data class PublishedNotebook(

        @Id
        @GeneratedValue
        val id: UUID? = null,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val author: User,

        val lastUpdatedTimestamp: Long,

        val createdTimestamp: Long,

        val title: String,

        val description: String? = null,

        @OneToMany(mappedBy = "notebook")
        val notes: List<PublishedNote> = listOf(),

        @ManyToOne
        @JoinColumn(name = "university_id")
        val university: University ?= null,

        @OneToMany(mappedBy = "notebook", cascade = [CascadeType.ALL])
        val comments: List<Comment> = listOf(),

        @ManyToMany(mappedBy = "notebooks", cascade = [CascadeType.ALL])
        val tags: Set<Tag>,

        @ManyToOne
        @JoinColumn(name = "topic_id")
        val topic: Topic? = null

)

@Entity
data class PublishedNote(
        @Id
        @GeneratedValue
        val id: Long? = null,
        val title: String?,
        val content: String?,

        @ManyToOne
        @JoinColumn(name = "shared_id")
        val notebook: PublishedNotebook
)
package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.PublishedNoteResponse
import com.levinzonr.ezpad.domain.responses.PublishedNotebookDetail
import com.levinzonr.ezpad.domain.responses.PublishedNotebookResponse
import java.util.*
import javax.persistence.*


@Entity(name = "shared")
data class PublishedNotebook(

        @Id
        val id: String = UUID.randomUUID().toString(),

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

        @ManyToMany
        @JoinTable(name = "shared_tags",
                joinColumns = [JoinColumn(name = "shared_id")],
                inverseJoinColumns = [JoinColumn(name = "tag_name")])
        val tags: Set<Tag>,

        @ManyToOne
        @JoinColumn(name = "topic_id")
        val topic: Topic? = null

) {

        fun toResponse() : PublishedNotebookResponse {
                return PublishedNotebookResponse(
                        title = title,
                        notesCount = notes.size.toLong(),
                        author = author.toResponse(),
                        description = description,
                        tags = tags.map { it.name }.toSet(),
                        commentCount = comments.size,
                        id = id.toString(),
                        topic = topic?.name

                )
        }

        fun toDetailedResponse() : PublishedNotebookDetail {
                return PublishedNotebookDetail(
                        id = id,
                        notes = notes.map { it.toResponse() },
                        comments = comments.map { it.toResponse() },
                        title = title,
                        description = description,
                        author = author.toResponse(),
                        tags = tags.map { it.name }.toSet(),
                        topic = topic?.name
                )
        }
}

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
) {
        fun toResponse() : PublishedNoteResponse {
                return PublishedNoteResponse(title, content)
        }
}
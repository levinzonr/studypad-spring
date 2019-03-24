package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.PublishedNoteResponse
import com.levinzonr.ezpad.domain.responses.PublishedNotebookDetail
import com.levinzonr.ezpad.domain.responses.PublishedNotebookResponse
import com.levinzonr.ezpad.security.StudyPadUserDetails
import java.util.*
import javax.persistence.*


@Entity(name = "shared")
class PublishedNotebook(

        author: User,

        val lastUpdatedTimestamp: Long,

        val createdTimestamp: Long,

        val title: String,

        val description: String? = null,

        notes: List<Note> = listOf(),
        val languageCode: String? = null,

        val excludeFromSearch: Boolean = false,



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

) : BaseNotebook(author = author, notes = notes) {

        fun toResponse(user: StudyPadUserDetails) : PublishedNotebookResponse {
                return PublishedNotebookResponse(
                        title = title,
                        notesCount = notes.size.toLong(),
                        author = author.toAuthorResponse(),
                        description = description,
                        tags = tags.map { it.name }.toSet(),
                        commentCount = comments.size,
                        id = id.toString(),
                        topic = topic?.name,
                        lastUpdated = lastUpdatedTimestamp,
                        languageCode = languageCode,
                        authoredByMe = user.id == author.id
                )
        }

        fun toDetailedResponse(user: StudyPadUserDetails) : PublishedNotebookDetail {
                return PublishedNotebookDetail(
                        id = id,
                        notes = notes.map { it.toResponse() },
                        comments = comments.sortedByDescending { it.dateCreated }.map { it.toResponse() },
                        title = title,
                        description = description,
                        author = author.toAuthorResponse(),
                        tags = tags.map { it.name }.toSet(),
                        topic = topic?.name,
                        lastUpdate = lastUpdatedTimestamp,
                        languageCode = languageCode,
                        versionState = state?.toResponse()!!,
                        authoredByMe = author.id == user.id
                )
        }
}

@Entity
data class PublishedNote(
        @Id
        @GeneratedValue
        val id: Long? = null,
        val title: String?,

        @Column(columnDefinition = "TEXT")
        val content: String?,

        @ManyToOne
        @JoinColumn(name = "shared_id")
        val notebook: PublishedNotebook
) {
        fun toResponse() : PublishedNoteResponse {
                return PublishedNoteResponse(title, content)
        }
}
package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.CommentResponse
import java.util.*
import javax.persistence.*

@Entity
data class Comment(

        @Id
        @GeneratedValue
        val id: Long? = null,

        @OneToOne
        @JoinColumn(name ="user_id")
        val author: User,

        @Column(columnDefinition = "TEXT")
        val content: String,

        val dateCreated: Long = Date().time,

        @ManyToOne
        @JoinColumn(name = "shared_id")
        val notebook: PublishedNotebook,

        val edited: Boolean = false
) {

        fun toResponse() : CommentResponse {
                return CommentResponse(
                        author = author.toResponse(),
                        content = content,
                        dateCreated = dateCreated,
                        id = id!!,
                        edited = edited
                )
        }
}
package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.NoteResponse
import java.util.*
import javax.persistence.*

@Entity
data class Note(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val title: String = "",

        @Column(columnDefinition = "TEXT")
        val content: String = "",

        @ManyToOne
        @JoinColumn(name = "notebook_id")
        val notebook: BaseNotebook,

        val sourceId: Long? = null
) {

        override fun toString(): String {
                return "Note[id: $id, sourceId: $sourceId, title:$title]"
        }

        fun toResponse() : NoteResponse {
                return NoteResponse(
                        id = id!!,
                        title = title ?: "",
                        content = content ?: "",
                        notebookId = notebook.id!!
                )
        }

        fun toBody() : NoteBody {
                return NoteBody(id, title, content, sourceId = sourceId)
        }
}

data class NoteBody(val id: Long?, val title: String?, val content: String?, val sourceId: Long? = null)
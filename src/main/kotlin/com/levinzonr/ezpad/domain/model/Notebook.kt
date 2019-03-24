package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.NotebookResponse
import javax.persistence.*
import com.levinzonr.ezpad.services.toGradient
import java.util.*

@Entity
class Notebook(

        val name: String,
        val colour: String? = null,

        id: String = UUID.randomUUID().toString(),

        state: VersionState? = null,

        user: User,

        notes: List<Note> = listOf(),



        val publishedVersionId: String? = null
) : BaseNotebook(id = id, author = user, notes = notes, state = state) {

        fun toResponse() : NotebookResponse {
                return NotebookResponse(
                        id = id,
                        name = name,
                        color = colour.toGradient(),
                        notesCount = notes.count(),
                        publishedNotebookId = publishedVersionId,
                        state = state?.toResponse()

                )
        }

        fun copy(name: String? = null, colour: String? = null, notes: List<Note>? = null, state: VersionState? = null, publishedVersionId: String? = null) : Notebook {
                return Notebook(name ?: this.name,
                        colour ?: this.colour,
                        notes = notes ?: this.notes,
                        user = this.author, id = id,
                        state = state,
                        publishedVersionId = publishedVersionId ?: this.publishedVersionId)
        }
}
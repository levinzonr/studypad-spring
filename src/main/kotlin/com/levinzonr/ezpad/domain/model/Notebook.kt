package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.responses.NotebookResponse
import javax.persistence.*
import com.levinzonr.ezpad.services.toGradient

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
) {

        fun toResponse() : NotebookResponse {
                return NotebookResponse(
                        id = id!!,
                        name = name,
                        color = colour.toGradient(),
                        notesCount = notes.count()

                )
        }
}
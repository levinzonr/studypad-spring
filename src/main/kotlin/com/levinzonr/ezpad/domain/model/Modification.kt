package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.responses.ModificationResponse
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
sealed class Modification(
        @Id
        val id: Long,

        @ManyToOne
        val state: VersionState

        ) {

    @Entity
    class Deleted(val noteId: Long, state: VersionState) : Modification(state = state, id = noteId)

    @Entity
    class Updated(val noteId: Long, val title: String, val content: String, state: VersionState) : Modification(state = state, id = noteId)

    @Entity
    class Added(val noteId: Long, val title: String, val content: String, state: VersionState) : Modification(state = state, id = noteId )

    fun toResponse() : ModificationResponse {
        return ModificationResponse(this.javaClass.simpleName)
    }
}


enum class ModificationType {
    DELETED, UPDATED, ADDED
}
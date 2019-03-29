package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.errors.InvalidPayloadException
import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.responses.ModificationResponse
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
sealed class Modification(
        @Id
        @GeneratedValue
        val id: Long? = null,

        val noteId: Long?,

        @ManyToOne
        val state: VersionState,

        @ManyToOne
        val author: User

) {

    @Entity
    class Deleted(noteId: Long, user: User, state: VersionState) : Modification(state = state, noteId = noteId, author = user)

    @Entity
    class Updated(

            noteId: Long,
            @Column(columnDefinition = "TEXT")
            val title: String, user: User,

            @Column(columnDefinition = "TEXT")
            val content: String,
            state: VersionState) : Modification(state = state, noteId = noteId, author = user)

    @Entity
    class Added(noteId: Long?,
                @Column(columnDefinition = "TEXT")
                val title: String, user: User,

                @Column(columnDefinition = "TEXT")
                val content: String, state: VersionState) : Modification(state = state, noteId = noteId, author = user)

    fun toResponse(): ModificationResponse {
        return ModificationResponse(this.javaClass.simpleName)
    }
}


enum class ModificationType {
    DELETED, UPDATED, ADDED;

    companion object {

        fun from(string: String): ModificationType {
            return when (string) {
                "add" -> ADDED
                "del" -> DELETED
                "upd" -> UPDATED
                else -> throw Exception(string)
            }
        }
    }

    fun toRepsonse(): String {
        return when (this) {
            DELETED -> "del"
            ADDED -> "add"
            UPDATED -> "upd"
        }
    }
}

val String.modType: ModificationType
    get() = ModificationType.from(this)
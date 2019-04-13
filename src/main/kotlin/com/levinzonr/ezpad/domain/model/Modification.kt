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
    class Added(id: Long? = null, noteId: Long?,
                @Column(columnDefinition = "TEXT")
                val title: String, user: User,

                @Column(columnDefinition = "TEXT")
                val content: String, state: VersionState) : Modification(id = id, state = state, noteId = noteId, author = user)

    fun toResponse(): ModificationResponse {
       return when(this) {
           is Added -> ModificationResponse(id!!, ModificationType.ADDED.toRepsonse(), title, content, author = author.toAuthorResponse())
           is Updated -> ModificationResponse(id!!, ModificationType.UPDATED.toRepsonse(), title, content, sourceId = noteId, author = author.toAuthorResponse())
           is Deleted -> ModificationResponse(id!!, ModificationType.DELETED.toRepsonse(), author = author.toAuthorResponse())
       }
    }

    override fun toString(): String {
        return "Mod ${javaClass.simpleName}, note $noteId, ${author.firstName}"
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

package com.levinzonr.ezpad.domain.model

import com.levinzonr.ezpad.domain.errors.BadRequestException
import java.util.*
import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
abstract class BaseNotebook(

        @Id
        val id: String = UUID.randomUUID().toString(),

        @ManyToOne
        @JoinColumn(name = "user_id")
        val author: User,

        @OneToMany(mappedBy = "notebook", cascade = [CascadeType.ALL])
        var notes: List<Note> = listOf(),

        @OneToOne(cascade = [CascadeType.ALL])
        var state: VersionState? = null
)


fun BaseNotebook.checkWritePolicy(user: User) {
        if (author.id != user.id) throw BadRequestException("Can't update something that's not yours")
}

fun BaseNotebook.checkWritePolicy(userId: String) {
        if (author.id != userId) throw BadRequestException("Can't update something that's not yours")
}

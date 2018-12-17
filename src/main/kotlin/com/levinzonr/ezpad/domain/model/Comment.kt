package com.levinzonr.ezpad.domain.model

import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne

data class Comment(
        val id: Long,

        @OneToOne
        @JoinColumn(name ="user_id")
        val author: User,

        @ManyToOne
        @JoinColumn(name = "shared_id")
        val notebook: PublishedNotebook
)
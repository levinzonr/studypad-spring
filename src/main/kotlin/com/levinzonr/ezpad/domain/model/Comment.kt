package com.levinzonr.ezpad.domain.model

import javax.persistence.*

@Entity
data class Comment(

        @Id
        @GeneratedValue()
        val id: Long,

        @OneToOne
        @JoinColumn(name ="user_id")
        val author: User,

        @ManyToOne
        @JoinColumn(name = "shared_id")
        val notebook: PublishedNotebook
)
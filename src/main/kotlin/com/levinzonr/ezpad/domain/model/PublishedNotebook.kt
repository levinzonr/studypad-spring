package com.levinzonr.ezpad.domain.model

import javax.persistence.*

// TODO Topic

@Entity(name = "shared")
data class PublishedNotebook(

        @Id
        @GeneratedValue
        val id: String? = null,

        @ManyToOne
        @JoinColumn(name = "user_id")
        val author: User,

        val lastUpdatedTimestamp: Long,
        val createdTimestamp: Long,
        val title: String,



        // val university: University,

        @OneToMany(mappedBy = "notebook", cascade = [CascadeType.ALL])
        val comments: List<Comment>

)
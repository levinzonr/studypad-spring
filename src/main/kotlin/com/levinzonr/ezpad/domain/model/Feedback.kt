package com.levinzonr.ezpad.domain.model

import javax.persistence.*

@Entity
data class Feedback(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,

        @OneToOne
        @JoinColumn(name ="user_id")
        val user: User,

        val appVersionName: String,
        val appVersionCode: Int,
        val androidVersion: String,
        val device: String,

        @Column(columnDefinition = "TEXT")
        val feedback: String
)
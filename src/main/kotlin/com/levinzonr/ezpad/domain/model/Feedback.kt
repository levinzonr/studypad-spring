package com.levinzonr.ezpad.domain.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToOne

@Entity
data class Feedback(
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
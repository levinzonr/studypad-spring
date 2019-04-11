package com.levinzonr.ezpad.domain.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class NotificationPayload(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val body: String,
        val title: String,
        val userId: String,
        val type: String,
        var read: Boolean,
        val notebookId: String,
        val createdAt: Long = System.currentTimeMillis())
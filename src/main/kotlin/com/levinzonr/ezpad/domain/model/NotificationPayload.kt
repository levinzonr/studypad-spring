package com.levinzonr.ezpad.domain.model

import javax.persistence.*

@Entity
data class NotificationPayload(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val userId: String,
        val title: String,
        val body: String,
        val type: String,

        @Embedded
        val notebookInfo: NotebookInfo,

        @Embedded
        val userInfo: UserInfo? = null,
        var read: Boolean = false,
        val createdAt: Long = System.currentTimeMillis())


@Embeddable
data class NotebookInfo(val notebookId: String, val notebookName: String)
@Embeddable
data class UserInfo(val authorId: String, val authorName: String)
package com.levinzonr.ezpad.domain.model

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class Notification(
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long? = null,
        val body: String,
        val userId: String,
        val type: String,
        val read: Boolean,
        val notebookId: String) {

    fun toHashMap() : HashMap<String, String> {
        return hashMapOf(
                "body" to body,
                "userId" to userId,
                "type" to type,
                "read" to read.toString(),
                "notebookId" to notebookId)
    }
}
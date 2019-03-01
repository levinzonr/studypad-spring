package com.levinzonr.ezpad.domain.responses

import com.levinzonr.ezpad.domain.model.Topic


data class PublishedNotebookDetail(
        val id: String,
        val author: AuthorResponse,
        val comments: List<CommentResponse>,
        val title: String,
        val description: String?,
        val notes: List<PublishedNoteResponse>,
        val tags: Set<String>,
        val topic: String?,
        val lastUpdate: Long,
        val languageCode: String?,
        var status: String = STATE_NEW // save, update

) {
    companion object {
        const val STATE_SAVED = "update"
        const val STATE_NEW = "save"
    }
}


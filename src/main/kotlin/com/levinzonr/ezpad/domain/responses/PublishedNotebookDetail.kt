package com.levinzonr.ezpad.domain.responses

import com.levinzonr.ezpad.domain.model.Topic


data class PublishedNotebookDetail(
        val id: String,
        val author: AuthorResponse,
        val comments: List<CommentResponse>,
        val title: String,
        val description: String?,
        val notes: List<NoteResponse>,
        val tags: Set<String>,
        val topic: String?
)
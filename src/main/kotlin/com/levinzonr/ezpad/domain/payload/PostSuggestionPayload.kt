package com.levinzonr.ezpad.domain.payload

data class PostSuggestionPayload(
        val noteId: Long? = null,
        val newContent: String? = null,
        val newTitle: String? = null
)
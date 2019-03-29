package com.levinzonr.ezpad.domain.responses

data class ModificationResponse(
        val type: String,
        val title: String? = null,
        val content: String? = null,
        val sourceId: Long? = null,
        val author: AuthorResponse
)
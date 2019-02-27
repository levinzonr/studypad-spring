package com.levinzonr.ezpad.domain.payload

class PublishedNotebookPayload(
        val title: String?,
        val universityId: Long?,
        val tags: Set<String>?,
        val topic: Long?,
        val description: String?,
        val notebookId: Long,
        val languageCode: String?

)
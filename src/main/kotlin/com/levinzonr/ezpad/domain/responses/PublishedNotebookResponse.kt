package com.levinzonr.ezpad.domain.responses

import com.levinzonr.ezpad.domain.model.ExportedUniversity

class PublishedNotebookResponse(
        val title: String,
        val notesCount: Long,
        val id: String,
        val description: String?,
        val author: AuthorResponse,
        val tags: Set<String>,
        val commentCount: Int,
        val topic: String?,
        val lastUpdated: Long,
        val languageCode: String?,
        val authoredByMe: Boolean,
        val university: UniversityResponse? = null

)
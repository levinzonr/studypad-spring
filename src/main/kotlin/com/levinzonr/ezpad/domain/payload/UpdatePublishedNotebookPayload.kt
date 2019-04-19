package com.levinzonr.ezpad.domain.payload

import com.levinzonr.ezpad.domain.model.ExportedUniversity

data class UpdatePublishedNotebookPayload(
        val title: String?,
        val description: String?,
        val languageCode: String?,
        val topicId: Long?,
        val universityId: Long?,
        val tags: Set<String>?
)
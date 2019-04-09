package com.levinzonr.ezpad.domain.responses

data class SectionResponse(
        val type: String,
        val items: List<PublishedNotebookResponse>
)
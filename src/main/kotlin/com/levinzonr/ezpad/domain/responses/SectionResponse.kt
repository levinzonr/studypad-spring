package com.levinzonr.ezpad.domain.responses

data class SectionResponse(
        val title: String,
        val items: List<PublishedNotebookResponse>
)
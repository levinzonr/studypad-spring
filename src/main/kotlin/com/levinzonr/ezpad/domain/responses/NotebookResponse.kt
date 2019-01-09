package com.levinzonr.ezpad.domain.responses

data class NotebookResponse (
        val id: Long,
        val name: String,
        val color: GradientColorResponse,
        val notesCount: Int,
        val exportedId: String?,
        val sourceId: String?
)
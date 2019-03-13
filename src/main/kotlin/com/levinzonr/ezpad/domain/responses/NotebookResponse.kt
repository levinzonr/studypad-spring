package com.levinzonr.ezpad.domain.responses

data class NotebookResponse (
        val id: String,
        val name: String,
        val color: GradientColorResponse,
        val notesCount: Int
)
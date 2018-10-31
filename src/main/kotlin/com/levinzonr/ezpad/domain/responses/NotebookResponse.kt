package com.levinzonr.ezpad.domain.responses

data class NotebookResponse (
        val id: Long,
        val name: String,
        val color: String,
        val notesCount: Int
)
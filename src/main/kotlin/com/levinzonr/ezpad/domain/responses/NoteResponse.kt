package com.levinzonr.ezpad.domain.responses

data class NoteResponse(
        val id: Long,
        val title: String,
        val content: String
)
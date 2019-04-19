package com.levinzonr.ezpad.domain.responses

import com.levinzonr.ezpad.domain.model.VersionState

data class NotebookResponse (
        val id: String,
        val name: String,
        val color: GradientColorResponse,
        val notesCount: Int,
        val publishedNotebookId: String?,
        val state: VersionStateResponse?,
        val authoredByMe: Boolean
)
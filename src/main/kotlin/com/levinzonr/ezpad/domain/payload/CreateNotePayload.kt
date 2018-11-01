package com.levinzonr.ezpad.domain.payload

import javax.validation.constraints.NotNull

data class CreateNotePayload(
        @NotNull
        val notebookId: Long,
        val title: String?,
        val content: String?

)
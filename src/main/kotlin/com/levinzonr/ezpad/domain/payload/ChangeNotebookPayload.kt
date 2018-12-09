package com.levinzonr.ezpad.domain.payload

import com.levinzonr.ezpad.domain.responses.GradientColorResponse

data class ChangeNotebookPayload(

    val name: String?,
    val gradientColorResponse: GradientColorResponse?
)
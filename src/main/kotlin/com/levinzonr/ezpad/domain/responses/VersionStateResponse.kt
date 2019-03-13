package com.levinzonr.ezpad.domain.responses

import com.levinzonr.ezpad.domain.model.Modification

data class VersionStateResponse(
        val version: Int,
        val modifications: List<ModificationResponse>
)
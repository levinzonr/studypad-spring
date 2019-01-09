package com.levinzonr.ezpad.domain.payload

data class CreateUniversityPayload(
    val fullName: String?,
    val shortName: String?,
    val aliases: Set<String> = setOf()
)
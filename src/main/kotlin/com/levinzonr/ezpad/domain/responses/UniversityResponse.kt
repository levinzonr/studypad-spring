package com.levinzonr.ezpad.domain.responses

data class UniversityResponse(
        val fullName: String,
        val location: Location,
        val id: Long
)

data class Location(
        val country: String,
        val code: String
)
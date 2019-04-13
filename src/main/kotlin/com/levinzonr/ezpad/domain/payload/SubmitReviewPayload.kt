package com.levinzonr.ezpad.domain.payload

data class SubmitReviewPayload(
        val approved: List<Long>,
        val rejected: List<Long>
)
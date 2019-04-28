package com.levinzonr.ezpad.domain.payload

data class UserFeedbackPayload(
        val appVersionName: String,
        val appVersionCode: Int,
        val androidVersion: String,
        val device: String,
        val feedback: String
)
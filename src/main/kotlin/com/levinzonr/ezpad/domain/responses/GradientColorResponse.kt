package com.levinzonr.ezpad.domain.responses

data class GradientColorResponse(
        val startColor: String,
        val endColor: String
) {

    companion object {
        val default
            get() = GradientColorResponse("#000428", "#004e92")
    }

}
package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.responses.GradientColorResponse
import org.springframework.stereotype.Service
import java.util.*

@Service
class ColorsServiceImpl : ColorsService {

    private val colorsList: List<String> = listOf(
            "#f5f7fa,#c3cfe2",
            "#89f7fe,#66a6ff",
            "#13547a,#80d0c7",
            "#67b26f,#4ca2cd",
            "#f3904f,#3b4371",
            "#ee0979,#ff6a00",
            "#ff00cc,#333399",
            "#56ab2f,#a8e063",
            "#000428,#004e92"

    )

    override fun getColors(): List<GradientColorResponse> {
        return colorsList.map { it.toGradient() }
    }

    override fun getRandomColor(): String {
        val ind = Random().nextInt(colorsList.size)
        return colorsList[ind]
    }

}

fun String?.toGradient(): GradientColorResponse {
    this ?: return GradientColorResponse.default
    val colors = trim().split(",")
    return if (colors.size == 2) {
        GradientColorResponse(colors.first(), colors[1])
    } else {
        GradientColorResponse.default
    }
}

fun GradientColorResponse?.asString() : String? {
    this ?: return null
    return "$startColor,$endColor"
}
package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.responses.GradientColorResponse

interface ColorsService {


    fun getColors() : List<GradientColorResponse>

    fun getRandomColor() : String
}
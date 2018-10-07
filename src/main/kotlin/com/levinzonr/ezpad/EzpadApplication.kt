package com.levinzonr.ezpad

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EzpadApplication

fun main(args: Array<String>) {
    runApplication<EzpadApplication>(*args)
}

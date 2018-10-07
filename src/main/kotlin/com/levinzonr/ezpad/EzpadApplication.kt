package com.levinzonr.ezpad

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class EzpadApplication {

    //Using generated security password: d571634d-2999-4b80-a80d-022b0600e511
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(EzpadApplication::class.java, *args)
        }
    }

}

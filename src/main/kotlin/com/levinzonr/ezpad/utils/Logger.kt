package com.levinzonr.ezpad.utils

object Logger {

    fun log(any: Any, message: String) {
        println("Logger of ${any::class.java.simpleName}: $message")
    }
}
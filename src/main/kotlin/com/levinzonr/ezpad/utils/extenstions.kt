package com.levinzonr.ezpad.utils

import java.net.URI
import java.util.*
import javax.servlet.http.HttpServletRequest

val HttpServletRequest.baseUrl: String
get() {
    val uri = URI.create(requestURL.toString())
    return "${uri.scheme}://${uri.authority}"
}


fun <T> Optional<T>.tryGet() : T? {
   return try {
       get()
   } catch (e: Exception) {
       null
   }
}
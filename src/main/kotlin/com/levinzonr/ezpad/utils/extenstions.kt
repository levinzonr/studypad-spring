package com.levinzonr.ezpad.utils

import java.net.URI
import javax.servlet.http.HttpServletRequest

val HttpServletRequest.baseUrl: String
get() {
    val uri = URI.create(requestURL.toString())
    return "${uri.scheme}://${uri.authority}"
}
package com.levinzonr.ezpad.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.net.URI
import javax.servlet.http.HttpServletRequest

val HttpServletRequest.baseUrl: String
get() {
    val uri = URI.create(requestURL.toString())
    return "${uri.scheme}://${uri.authority}"
}

inline fun <reified T> Gson.fromJson(json: String) = this.fromJson<T>(json, object: TypeToken<T>() {}.type)

package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.model.TokenResponse
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import java.nio.charset.Charset
import java.util.*

object RestAuthHelper {

    fun authRedirect(url: String, username: String, password: String) : TokenResponse {
        val map = LinkedMultiValueMap<String, String>()
        map.add("username", username)
        map.add("password", password)
        map.add("grant_type", "password")
        val request = HttpEntity<MultiValueMap<String, String>>(map, createHeaders("ezpad-mobile-client", "ccUyb6vS4S8nxfbKPCrN"))
        val restTemplate = RestTemplate()
        return restTemplate.postForObject("$url/oauth/token", request, TokenResponse::class.java)!!

    }

    private fun createHeaders(username: String, password: String): HttpHeaders {
        return object : HttpHeaders() {
            init {
                val auth = "$username:$password"
                val encodedAuth = Base64.getEncoder().encode(
                        auth.toByteArray(Charset.forName("US-ASCII")))
                val authHeader = "Basic " + String(encodedAuth)
                set("Authorization", authHeader)
                contentType = MediaType.APPLICATION_FORM_URLENCODED
            }
        }
    }


}
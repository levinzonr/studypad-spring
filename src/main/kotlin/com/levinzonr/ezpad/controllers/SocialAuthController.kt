package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.model.TokenResponse
import com.levinzonr.ezpad.domain.payload.FacebookLogin
import com.levinzonr.ezpad.domain.payload.FacebookUser
import com.levinzonr.ezpad.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.social.facebook.api.impl.FacebookTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import javax.validation.Valid
import java.net.InetAddress
import java.net.URI
import java.net.URL
import javax.inject.Inject
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED
import org.apache.catalina.manager.StatusTransformer.setContentType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.security.jwt.JwtHelper.headers
import org.springframework.util.MultiValueMap
import org.springframework.http.HttpEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.util.LinkedMultiValueMap
import java.nio.charset.Charset
import java.util.*


@RestController
@RequestMapping("/auth")
class SocialAuthController {

    companion object {
        var fields = arrayOf("id", "email", "first_name", "last_name")
    }

    @Autowired
    private lateinit var userService: UserService

    @PostMapping("/facebook")
    fun loginViaFacebook(
            request: HttpServletRequest,
            @Valid @RequestBody facebookLogin: FacebookLogin) : TokenResponse {

        val fbTemplate = FacebookTemplate(facebookLogin.token)
        val fbUser = fbTemplate.fetchObject("me", FacebookUser::class.java, *(fields))
        println(fbUser.toString())
        val user = (userService.processFacebookUser(fbUser))
        val uri = URI.create(request.requestURL.toString())
        val url = "${uri.scheme}://${uri.authority}"
        print(url)

        val headers = createHeaders("ezpad-mobile-client", "ccUyb6vS4S8nxfbKPCrN")

        val map = LinkedMultiValueMap<String, String>()
        map.add("username", user.email)
        map.add("password", fbUser.id )
        map.add("grant_type", "password")
        val request = HttpEntity<MultiValueMap<String, String>>(map, headers)


        val restTemplate = RestTemplate()
        return restTemplate.postForObject("$url/oauth/token", request, TokenResponse::class.java)!!
    }


    fun createHeaders(username: String, password: String): HttpHeaders {
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
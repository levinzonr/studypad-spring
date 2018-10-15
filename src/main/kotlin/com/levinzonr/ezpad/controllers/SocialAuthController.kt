package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.payload.FacebookLogin
import com.levinzonr.ezpad.domain.payload.FacebookUser
import org.springframework.social.facebook.api.impl.FacebookTemplate
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestTemplate
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class SocialAuthController {

    companion object {
        var fields = arrayOf("id", "email", "first_name", "last_name")
    }

    @PostMapping("/facebook")
    fun loginViaFacebook(@Valid @RequestBody facebookLogin: FacebookLogin) {

        val restTemplate = FacebookTemplate(facebookLogin.token)
        val fbUser = restTemplate.fetchObject("me", FacebookUser::class.java, *(fields))
        println(fbUser.toString())

    }

}
package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.payload.CreateUserPayload
import com.levinzonr.ezpad.domain.responses.FirebaseTokenResponse
import com.levinzonr.ezpad.domain.responses.UserResponse
import com.levinzonr.ezpad.services.AuthenticationService
import com.levinzonr.ezpad.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.query.Param
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/auth")
class AuthenticationController {


    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var authenticationService: AuthenticationService


    @PostMapping("/register")
    fun createUser(@RequestHeader("Locale") locale: String, @RequestBody createUserPayload: CreateUserPayload) : FirebaseTokenResponse {
        val user = userService.createUser(
                createUserPayload.email!!,
                createUserPayload.password!!,
                createUserPayload.firstName,
                createUserPayload.lastName,
                locale)

        return FirebaseTokenResponse(authenticationService.createCustomToken(user.id!!), user.toResponse(0))
    }

    @PostMapping("/login")
    fun loginUsingFirebaseToken(@RequestHeader("Locale") locale: String, @Param ("token") token: String) : UserResponse {
        val uuid  = authenticationService.userIdFromToken(token)
        val user = userService.findUserById(uuid) ?: userService.createUser(uuid, locale = locale)
        return user.toResponse(0)
    }



}
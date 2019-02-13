package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.payload.CreateUserPayload
import com.levinzonr.ezpad.domain.responses.UserResponse
import com.levinzonr.ezpad.services.AuthenticationService
import com.levinzonr.ezpad.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/auth")
class AuthenticationController {


    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var authenticationService: AuthenticationService


    @PostMapping("/register")
    fun createUser(@RequestBody createUserPayload: CreateUserPayload) : String {
        val user = userService.createUser(
                createUserPayload.email!!,
                createUserPayload.password!!,
                createUserPayload.firstName,
                createUserPayload.lastName)

        return authenticationService.createCustomToken(user.id!!)
    }

    @PostMapping("/login")
    fun loginUsingFirebaseToken(token: String) : UserResponse {
        val uuid  = authenticationService.userIdFromToken(token)
        val user = userService.findUserById(uuid) ?: userService.createUser(uuid)
        return user.toResponse()
    }



}
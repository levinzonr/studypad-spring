package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.responses.UserResponse
import com.levinzonr.ezpad.domain.payload.CreateUserPayload
import com.levinzonr.ezpad.domain.payload.UpdateUserPayload
import com.levinzonr.ezpad.security.EzpadUserDetails
import com.levinzonr.ezpad.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserRestController {

    @Autowired
    private lateinit var userService: UserService

    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal userDetails: EzpadUserDetails): UserResponse {
        return userService.getUserById(userDetails.userId).toResponse()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewUser(@Valid @RequestBody payload: CreateUserPayload): UserResponse {
        val user = userService.createUser(payload.email!!, payload.password!!, payload.firstName, payload.lastName, null)
        return user.toResponse()
    }


    @GetMapping("/{userId}")
    fun getUserById(@PathVariable("userId") userId: Long): UserResponse {
        return userService.getUserById(userId).toResponse()
    }


    @PostMapping("/me")
    fun updateCurrentUserProfile(@AuthenticationPrincipal userDetails: EzpadUserDetails,
                                 @Valid @RequestBody updateUserPayload: UpdateUserPayload): UserResponse {
        return userService.updateUserById(userDetails.userId,
                updateUserPayload.firstName,
                updateUserPayload.lastName,
                updateUserPayload.password).toResponse()
    }

}
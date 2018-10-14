package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.dto.FieldError
import com.levinzonr.ezpad.domain.dto.UserResponse
import com.levinzonr.ezpad.domain.errors.InvalidPayloadException
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.payload.CreateUserPayload
import com.levinzonr.ezpad.security.EzpadUserDetails
import com.levinzonr.ezpad.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.crossstore.ChangeSetPersister
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
    fun getCurrentUser(@AuthenticationPrincipal userDetails: EzpadUserDetails) : UserResponse {
        userDetails.userId?.let {
            return userService.getUserById(it).toDto()
        }
        throw InvalidPayloadException(listOf(FieldError("uuid", "Auth id is missing")))
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewUser(@Valid @RequestBody payload: CreateUserPayload) : UserResponse {
        val user =  userService.createUser(payload.email, payload.password, payload.firstName, payload.lastName ,null)
        return user.toDto()
    }

}
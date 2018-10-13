package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.dto.UserDto
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
    fun getCurrentUser(@AuthenticationPrincipal userDetails: EzpadUserDetails) : UserDto {
        val user = userService.getUserById(userDetails.userId)
                .orElseThrow { ChangeSetPersister.NotFoundException() }
        return user.toDto()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewUser(@Valid @RequestBody payload: CreateUserPayload) : UserDto {
        val user = userService.createUser(payload.email, payload.password, payload.firstName, payload.lastName ,null)
        if (user == null) {
            throw Exception("Error")
        } else {
            return user.toDto()
        }
    }

}
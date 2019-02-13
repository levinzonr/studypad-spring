package com.levinzonr.ezpad.controllers

import com.google.firebase.auth.FirebaseAuth
import com.levinzonr.ezpad.domain.model.TokenResponse
import com.levinzonr.ezpad.domain.responses.UserResponse
import com.levinzonr.ezpad.domain.payload.CreateUserPayload
import com.levinzonr.ezpad.domain.payload.FinishSignupPayload
import com.levinzonr.ezpad.domain.payload.UpdateUserPayload
import com.levinzonr.ezpad.security.StudyPadUserDetails
import com.levinzonr.ezpad.security.firebase.FirebaseAuthToken
import com.levinzonr.ezpad.services.NotebookService
import com.levinzonr.ezpad.services.NotesService
import com.levinzonr.ezpad.services.UserService
import com.levinzonr.ezpad.utils.baseUrl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import javax.servlet.ServletRequest
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserRestController {

    @Autowired
    private lateinit var userService: UserService



    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal userDetails: StudyPadUserDetails): UserResponse {
        val auth = SecurityContextHolder.getContext().authentication
        (auth.principal as FirebaseAuthToken).token
        return userService.findUserById(userDetails.userId)!!.toResponse()
    }




    @GetMapping("/{userId}")
    fun getUserById(@PathVariable("userId") userId: String): UserResponse {
        return userService.findUserById(userId)!!.toResponse()
    }


}
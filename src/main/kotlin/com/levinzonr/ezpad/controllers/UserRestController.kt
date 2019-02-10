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

    @Autowired
    private lateinit var notebookService: NotebookService

    @Autowired
    private lateinit var noteService: NotesService

    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal userDetails: StudyPadUserDetails): UserResponse {
        val auth = SecurityContextHolder.getContext().authentication
        (auth.principal as FirebaseAuthToken).token
        return userService.getUserById(userDetails.userId).toResponse()
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createNewUser(@Valid @RequestBody payload: CreateUserPayload,
                      req: HttpServletRequest): TokenResponse {
        val user = userService.createUser(payload.email!!, payload.password!!, payload.firstName, payload.lastName, null)
        val nb = notebookService.createNewNotebook("Default", user)
        noteService.createNote("Test", "Content", nb)
        val token = FirebaseAuth.getInstance().createCustomToken(user.id)
        return TokenResponse(access_token = token, user = user.toResponse())
    }


    @GetMapping("/{userId}")
    fun getUserById(@PathVariable("userId") userId: String): UserResponse {
        return userService.getUserById(userId).toResponse()
    }


    @PostMapping("/me")
    fun updateCurrentUserProfile(@AuthenticationPrincipal userDetails: StudyPadUserDetails,
                                 @Valid @RequestBody updateUserPayload: UpdateUserPayload): UserResponse {
        return userService.updateUserById(userDetails.userId,
                updateUserPayload.firstName,
                updateUserPayload.lastName,
                updateUserPayload.password).toResponse()
    }

    @PostMapping("/signup/finish")
    fun updateUserUniversity(
            @Valid @RequestBody finishSignup: FinishSignupPayload,
            @AuthenticationPrincipal userDetails: StudyPadUserDetails) : UserResponse {

        println("Update $finishSignup")
        return userService.updateUserUniversity(userDetails.userId, finishSignup.universityId!!).toResponse()

    }

}
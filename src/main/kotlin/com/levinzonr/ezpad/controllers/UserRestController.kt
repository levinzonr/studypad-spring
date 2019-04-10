package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.model.NotificationPayload
import com.levinzonr.ezpad.domain.payload.FinishSignupPayload
import com.levinzonr.ezpad.domain.responses.UserResponse
import com.levinzonr.ezpad.security.StudyPadUserDetails
import com.levinzonr.ezpad.security.firebase.FirebaseAuthToken
import com.levinzonr.ezpad.services.MessageService
import com.levinzonr.ezpad.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/users")
class UserRestController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var messageService: MessageService


    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal details: StudyPadUserDetails): UserResponse {
        return userService.findUserById(details.userId)!!.toResponse()
    }




    @GetMapping("/{userId}")
    fun getUserById(@PathVariable("userId") userId: String): UserResponse {
        return userService.findUserById(userId)!!.toResponse()
    }

    @PostMapping("/signup/finish")
    fun updateUserUniversity(
            @Valid @RequestBody finishSignup: FinishSignupPayload,
            @AuthenticationPrincipal userDetails: StudyPadUserDetails) : UserResponse {

        println("Update $finishSignup")
        return userService.updateUser(userDetails.userId, finishSignup.universityId).toResponse()

    }

    @PostMapping("/notifications/register")
    fun registerFirebaseToken(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestParam("token") token: String) {
        userService.registerFirebaseToken(userDetails.userId, token)
    }


    @GetMapping("/me/notifications")
    fun getLatestNotificatios(@AuthenticationPrincipal userDetails: StudyPadUserDetails) : List<NotificationPayload> {
        return messageService.getUserNotifications(userDetails.userId)
    }


}
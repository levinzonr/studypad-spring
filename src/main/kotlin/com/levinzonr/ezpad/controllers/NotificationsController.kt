package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.model.NotificationPayload
import com.levinzonr.ezpad.security.StudyPadUserDetails
import com.levinzonr.ezpad.services.MessageService
import com.levinzonr.ezpad.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/api/notifications")
class NotificationsController {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var messageService: MessageService

    @PostMapping("/register")
    fun registerFirebaseToken(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestParam("token") token: String) {
        userService.registerFirebaseToken(userDetails.userId, token)
    }


    @GetMapping
    fun getLatestNotifications(@AuthenticationPrincipal userDetails: StudyPadUserDetails) : List<NotificationPayload> {
        return messageService.getUserNotifications(userDetails.userId)
    }


    @PostMapping("/read")
    fun markNotificationsAsRead(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestParam("ids") list: List<Long>) {
        messageService.markNotificationsAsRead(userDetails.userId, list)
    }

}
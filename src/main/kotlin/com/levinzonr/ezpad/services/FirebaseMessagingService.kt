package com.levinzonr.ezpad.services

import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.levinzonr.ezpad.domain.model.Notification
import com.levinzonr.ezpad.domain.model.PublishedNotebook
import com.levinzonr.ezpad.domain.repositories.NotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FirebaseMessagingService : MessageService {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var messageService: FirebaseMessaging

    @Autowired
    private lateinit var repository: NotificationRepository

    override fun notifyOnUpdate(publishedNotebook: PublishedNotebook) {
    }

    override fun notifyOnComment(publishedNotebook: PublishedNotebook) {
        val notification = Notification(body = "New comment", userId = publishedNotebook.author.id ?: "", type = "comment", read = false, notebookId = publishedNotebook.id)
        println(" Tokens ${publishedNotebook.author.firebaseTokens}")
        println(" Tokens ${userService.findUserById(publishedNotebook.author.id!!)?.firebaseTokens}")

        val remoteNotification = com.google.firebase.messaging.Notification("New comment", notification.body)
        val messages = publishedNotebook.author.firebaseTokens.map {
            println("Message fro token $it")
            Message.builder()
                    .putAllData(notification.toHashMap())
                    .setNotification(remoteNotification)
                    .setToken(it)
                    .build()
        }
        repository.save(notification)
        messages.forEach { messageService.send(it) }
    }

    override fun notifyOnSuggestionAdded(publishedNotebook: PublishedNotebook) {

    }


}
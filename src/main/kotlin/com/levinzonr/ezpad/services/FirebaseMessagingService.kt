package com.levinzonr.ezpad.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.google.gson.Gson
import com.levinzonr.ezpad.domain.ApiMessages
import com.levinzonr.ezpad.domain.model.NotificationPayload
import com.levinzonr.ezpad.domain.model.PublishedNotebook
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.NotificationRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FirebaseMessagingService : MessageService {

    @Autowired
    private lateinit var messageService: FirebaseMessaging

    @Autowired
    private lateinit var repository: NotificationRepository


    override fun notifyOnUpdate(publishedNotebook: PublishedNotebook, subscribers: List<User>) {
        val holder = Builder(publishedNotebook)
                .setRecepients(subscribers)
                .setType(ApiMessages.Notifications.Types.UPDATE)
                .build()
        repository.saveAll(holder.domainNotifications)
        holder.messages.forEach { messageService.send(it) }
    }

    override fun notifyOnComment(publishedNotebook: PublishedNotebook, commentAuthor: User) {
        val holder = Builder(publishedNotebook)
                .setRecepients(listOf(publishedNotebook.author))
                .setType(ApiMessages.Notifications.Types.COMMENT, commentAuthor)
                .build()
        repository.saveAll(holder.domainNotifications)
        holder.messages.forEach { messageService.send(it) }
    }

    override fun notifyOnSuggestionAdded(publishedNotebook: PublishedNotebook) {
        val holder = Builder(publishedNotebook)
                .setRecepients(listOf(publishedNotebook.author))
                .setType(ApiMessages.Notifications.Types.SUGGESTION)
                .build()
        repository.saveAll(holder.domainNotifications)
        holder.messages.forEach { messageService.send(it) }
    }


    inner class Builder(val notebook: PublishedNotebook) {

        private var title: String = ""
        private var message: String = ""
        private var type = ""
        private var recepients: List<User> = listOf()
        private lateinit var notification: Notification

        fun setType(type: String, user: User? = null): Builder {
            this.type = type
            when (type) {
                ApiMessages.Notifications.Types.COMMENT -> {
                    title = ApiMessages.Notifications.Messages.COMMENT_TITLE
                    message = ApiMessages.Notifications.Messages.COMMENT_MESSAGE
                }
                ApiMessages.Notifications.Types.SUGGESTION -> {
                    title = ApiMessages.Notifications.Messages.SUGGESTION_TITLE
                    message = ApiMessages.Notifications.Messages.SUGGESTION_MESSAGE
                }
                ApiMessages.Notifications.Types.UPDATE -> {
                    title = ApiMessages.Notifications.Messages.UPDATE_TITLE
                    message = ApiMessages.Notifications.Messages.UPDATE_MESSAGE
                }

            }
            user?.let { message = message.replace("[user]", it.displayName.toString()) }
            message = message.replace("[notebook]", notebook.title)
            notification = Notification(title, message)
            return this
        }

        fun setRecepients(list: List<User>): Builder {
            recepients = list
            return this
        }


        fun build(): NotificationHolder {
            val domainNotification = recepients.map { buildPayload(it) }
            val tokens = recepients.map { it.firebaseTokens }.flatten()
            val messages = buildMessages(tokens)
            return NotificationHolder(domainNotification, messages)
        }


        private fun buildPayload(user: User): NotificationPayload {
            return NotificationPayload(
                    body = message,
                    title = title,
                    userId = user.id!!,
                    read = false,
                    notebookId = notebook.id,
                    type = type)
        }

        private fun buildMessages(list: List<String>): List<Message> {
            val notificationString = Gson().toJson(notification)
            return list.map {
                Message.builder()
                        .setNotification(notification)
                        .setToken(it)
                        .putData("payload", notificationString)
                        .build()
            }
        }
    }
}


data class NotificationHolder(val domainNotifications: List<NotificationPayload>, val messages: List<Message>)


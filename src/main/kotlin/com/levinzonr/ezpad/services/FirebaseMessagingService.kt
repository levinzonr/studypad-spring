package com.levinzonr.ezpad.services

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import com.google.gson.Gson
import com.levinzonr.ezpad.domain.ApiMessages
import com.levinzonr.ezpad.domain.model.*
import com.levinzonr.ezpad.domain.repositories.NotificationRepository
import org.apache.tomcat.util.modeler.NotificationInfo
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
                .setRecepients(subscribers.filter { it.id != publishedNotebook.author.id })
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

        val payload = holder.domainNotifications.first()
        val payloadString = Gson().toJson(payload)
        val channelMessage = Message.builder()
                .putData("payload", payloadString)
                .setTopic("comments_${publishedNotebook.id}")
                .build()

        messageService.send(channelMessage)

        if (commentAuthor.id == publishedNotebook.author.id) return


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

    override fun markNotificationsAsRead(userId: String, list: List<Long>) {
        val toMark = repository.findAllById(list)
        toMark.forEach { it.read = true }
        repository.saveAll(toMark)
    }

    override fun getUserNotifications(userId: String, unreadOnly: Boolean): List<NotificationPayload> {
        return repository.findAll()
                .filter { it.userId == userId }
                .filter { if(unreadOnly) !it.read else true}
                .sortedByDescending { it.createdAt }
    }

    inner class Builder(val notebook: PublishedNotebook) {

        private var title: String = ""
        private var message: String = ""
        private var type = ""
        private var recepients: List<User> = listOf()
        private lateinit var notificationPayload: NotificationPayload
        private var showAsNotification = false

        private var userInfo : UserInfo? = null
        private val notebookInfo = NotebookInfo(notebook.id, notebook.title)

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

            user?.let {
                userInfo = UserInfo(it.id ?: "", it.displayName ?: "")
                message = message.replace("[name]", it.displayName.toString()) }
            message = message.replace("[notebook]", notebook.title)
            return this
        }

        fun setRecepients(list: List<User>): Builder {
            recepients = list
            return this
        }

        fun setShowAsNotification(isShow: Boolean) : Builder {
            this.showAsNotification = isShow
            return this
        }


        fun build(): NotificationHolder {
            val domainNotification = recepients.map { buildPayload(it) }

            notificationPayload = NotificationPayload(
                    body = message,
                    title = title,
                    read = false,
                    notebookInfo = notebookInfo,
                    userInfo = userInfo,
                    type = type,
                    userId = "")
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
                    notebookInfo = notebookInfo,
                    type = type)
        }

        private fun buildMessages(list: List<String>): List<Message> {
            val notificationString = Gson().toJson(notificationPayload)
            return list.map {
                Message.builder()
                        .setToken(it)
                        .putData("payload", notificationString)
                        .putData("showAsNotification", showAsNotification.toString())
                        .build()
            }
        }
    }
}


data class NotificationHolder(val domainNotifications: List<NotificationPayload>, val messages: List<Message>)


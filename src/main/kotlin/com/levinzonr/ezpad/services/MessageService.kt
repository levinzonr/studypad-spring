package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.NotificationPayload
import com.levinzonr.ezpad.domain.model.PublishedNotebook
import com.levinzonr.ezpad.domain.model.User

interface MessageService {

    fun notifyOnUpdate(publishedNotebook: PublishedNotebook, subscribers: List<User>)
    fun notifyOnComment(publishedNotebook: PublishedNotebook, commentAuthor: User)
    fun notifyOnSuggestionAdded(publishedNotebook: PublishedNotebook)
    fun getUserNotifications(userId: String, unreadOnly: Boolean = false) : List<NotificationPayload>
}
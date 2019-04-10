package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.PublishedNotebook

interface MessageService {

    fun notifyOnUpdate(publishedNotebook: PublishedNotebook)
    fun notifyOnComment(publishedNotebook: PublishedNotebook)
    fun notifyOnSuggestionAdded(publishedNotebook: PublishedNotebook)
}
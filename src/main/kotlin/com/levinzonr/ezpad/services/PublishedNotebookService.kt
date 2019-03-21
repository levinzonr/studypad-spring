package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Modification
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.PublishedNotebook
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.payload.PostSuggestionPayload

interface PublishedNotebookService {


    fun publishNotebook(userId: String,
                        notebookId: String,
                        languageCode: String? = null,
                        title: String? = null, description: String? = null,
                        topicId: Long?, tags: Set<String> = setOf(),
                        universityID: Long? = null): PublishedNotebook


    fun quickPublish(userId: String, notebookId: String) : PublishedNotebook

    fun getMostRelevant() : List<PublishedNotebook>

    fun getPublishedNotebookById(id: String) : PublishedNotebook

    fun createSuggestion(user: User, postSuggestionPayload: PostSuggestionPayload, notebookId: String)

    fun filterByTag(tag: String) : List<PublishedNotebook>

    fun filterByTopic(topic: String) : List<PublishedNotebook>

    fun approveModifications(id: String, modificationId: List<Long>)

    fun findNotebooks(tags: Set<String>, topic: String) : List<PublishedNotebook>

}
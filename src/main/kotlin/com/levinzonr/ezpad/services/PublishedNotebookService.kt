package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.*
import com.levinzonr.ezpad.domain.payload.PostSuggestionPayload
import com.levinzonr.ezpad.domain.responses.SectionResponse

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

    fun createSuggestion(user: User, postSuggestionPayload: PostSuggestionPayload, notebookId: String, multiple: Boolean = false)

    fun filterByTag(tag: String) : List<PublishedNotebook>

    fun searchNotebooks(query: String?, universityID: Long?, tags: Set<String>?, topics: List<Long>?, languageCode: String) : List<PublishedNotebook>

    fun filterByTopic(topic: String) : List<PublishedNotebook>

    fun approveModifications(userId: String, id: String, modificationId: List<Long>) : PublishedNotebook

    fun rejectModifications(userId: String, id: String, modificationId: List<Long>) : PublishedNotebook

    fun findNotebooks(tags: Set<String>, topic: String) : List<PublishedNotebook>

    fun applyLocalAuthorChanges(user: User, id: String) : PublishedNotebook

    fun migrateToSuggestions(user: User, notebookId: String) : PublishedNotebook

    fun handleChangesMigration(user: User, notebookId: String) : PublishedNotebook

    fun getFeed(user: User) : List<Section>

    fun getAll() : List<PublishedNotebook>

    fun getSubscribers(publishedNotebook: PublishedNotebook) : List<User>

}
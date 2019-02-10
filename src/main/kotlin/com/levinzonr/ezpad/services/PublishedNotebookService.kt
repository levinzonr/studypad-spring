package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.PublishedNotebook

interface PublishedNotebookService {


    fun publishNotebook(userId: String,
                        notebookId: Long,
                        title: String? = null, description: String? = null,
                        topicId: Long?, tags: Set<String> = setOf(), universityID: Long? = null): PublishedNotebook


    fun getMostRelevant() : List<PublishedNotebook>

    fun getPublishedNotebookById(id: String) : PublishedNotebook

    fun filterByTag(tag: String) : List<PublishedNotebook>

    fun filterByTopic(topic: String) : List<PublishedNotebook>

    fun findNotebooks(tags: Set<String>, topic: String) : List<PublishedNotebook>

}
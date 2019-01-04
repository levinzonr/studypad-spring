package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.PublishedNotebook

interface PublishedNotebookService {


    fun publishNotebook(userId: Long,
                        notebookId: Long,
                        title: String? = null, description: String? = null,
                        topicId: Long?, tags: Set<String> = setOf(), universityID: Long? = null): PublishedNotebook


    fun getMostRelevant() : List<PublishedNotebook>

    fun getPublishedNotebookById(id: String) : PublishedNotebook


}
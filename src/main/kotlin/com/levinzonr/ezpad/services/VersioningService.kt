package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.*

interface VersioningService {

    fun initLocalVersion(source: PublishedNotebook, notebook: Notebook)
    fun initPublishedVersion(publishedNotebook: PublishedNotebook)

    fun modify(state: VersionState?, note: Note, type: ModificationType)

    fun getModifications(notebookId: String) : List<Modification>

}
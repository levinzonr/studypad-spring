package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.*

interface VersioningService {

    fun initLocalVersion(source: PublishedNotebook, notebook: Notebook) : VersionState

    fun initPublishedVersion(publishedNotebook: PublishedNotebook) : VersionState

    fun modify(state: VersionState?, note: NoteBody, type: ModificationType, user: User? = null)

    fun getModifications(notebookId: String) : List<Modification>

    fun deleteModifications(list: List<Long>)

    fun applyModifications(state: VersionState?, list: List<Long>) : VersionState

    fun getModificationsByIds(list: List<Long>) : List<Modification>
}
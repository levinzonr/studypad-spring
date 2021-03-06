package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.model.VersionState

interface NotebookService {

    fun getUserNotebooks(user: User) : List<Notebook>

    fun createNewNotebook(name: String, user: User, authoredByMe: Boolean) : Notebook

    fun getNotebookDetails(id: String) : Notebook

    fun updateNotebook(id: String, name: String?, color: String?) : Notebook

    fun deleteNotebook(id: String)

    fun updateNotebook(notebook: Notebook) : Notebook

    fun updateState(notebook: Notebook, versionState: VersionState)

    fun createFromPublished(publishedId: String, userId: String) : Notebook

    fun createLocalCopy(publishedId: String, userId: String) : Notebook

    fun getAll() : List<Notebook>
}
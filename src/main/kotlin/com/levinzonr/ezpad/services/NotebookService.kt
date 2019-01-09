package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.User

interface NotebookService {

    fun getUserNotebooks(user: User) : List<Notebook>

    fun createNewNotebook(name: String, user: User) : Notebook

    fun getNotebookDetails(id: Long) : Notebook

    fun updateNotebook(id: Long, name: String?, color: String?) : Notebook

    fun deleteNotebook(id: Long)

    fun createFromPublished(publishedId: String, userId: Long) : Notebook
}
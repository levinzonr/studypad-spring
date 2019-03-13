package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.User

interface NotebookService {

    fun getUserNotebooks(user: User) : List<Notebook>

    fun createNewNotebook(name: String, user: User) : Notebook

    fun getNotebookDetails(id: String) : Notebook

    fun updateNotebook(id: String, name: String?, color: String?) : Notebook

    fun deleteNotebook(id: String)

    fun createFromPublished(publishedId: String, userId: Long) : Notebook

}
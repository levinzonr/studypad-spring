package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.User
import org.springframework.data.repository.CrudRepository

interface NotebooksRepository : CrudRepository<Notebook, String> {

    fun findByAuthor(author: User) : List<Notebook>
}
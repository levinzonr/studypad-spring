package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.model.Notebook
import org.springframework.data.repository.CrudRepository

interface NotesRepository : CrudRepository<Note, Long> {

    fun findByNotebook(notebook: Notebook) : List<Note>
    fun findByNotebookId(id: Long) : List<Note>

}
package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.BaseNotebook
import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.model.Notebook

interface NotesService {

    fun createNote(title: String, content: String, notebook: Notebook) : Note

    fun getNote(id: Long) : Note

    fun updateNote(id: Long, title: String?, content: String?) : Note

    fun deleteNote(id: Long)

    fun copyAndReplace(notes: List<Note>, notebook: BaseNotebook) : List<Note>



}
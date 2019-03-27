package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.BaseNotebook
import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.PublishedNotebook

interface NotesService {

    fun createNote(title: String, content: String, notebook: BaseNotebook) : Note

    fun getNote(id: Long) : Note

    fun updateNote(id: Long, title: String? = null, content: String? = null, sourceId: Long? = null) : Note

    fun deleteNote(id: Long)

    fun importNotes(notes: List<Note>, notebook: BaseNotebook) : List<Note>

    fun getNotesFromNotebook(notebookId: String) : List<Note>

    fun exportNotes(notes: List<Note>, publishedNotebook: PublishedNotebook)
}
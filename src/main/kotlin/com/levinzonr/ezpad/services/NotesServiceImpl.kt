package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.BaseNotebook
import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.PublishedNotebook
import com.levinzonr.ezpad.domain.repositories.NotesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotesServiceImpl : NotesService {

    @Autowired
    private lateinit var notesRepository: NotesRepository

    override fun createNote(title: String, content: String, notebook: BaseNotebook): Note {
        return notesRepository.save(Note(title = title, content = content, notebook = notebook))
    }

    override fun getNote(id: Long): Note {
        return notesRepository.findById(id).orElseThrow {
            NotFoundException.Builder(Note::class).buildWithId(id.toString())
        }
    }

    override fun updateNote(id: Long, title: String?, content: String?, sourceId: Long?): Note {
        val old = getNote(id)
        val newTitle = title ?: old.title
        val newContents = content ?: old.content
        val newSource = sourceId ?: old.sourceId
        println("Update with $newTitle")
        val note =  notesRepository.save(old.copy(title = newTitle, content = newContents, sourceId = newSource))
        println(note)
        return note
    }

    override fun deleteNote(id: Long) {
        return notesRepository.deleteById(id)
    }


    override fun exportNotes(notes: List<Note>, publishedNotebook: PublishedNotebook) {
        notes.forEach { note ->
            val createdNote = createNote(note.title ?: "", note.content ?:"" , publishedNotebook)
            notesRepository.save(note.copy(sourceId = createdNote.id))
        }
    }

    override fun importNotes(notes: List<Note>, notebook: Notebook): List<Note> {
        // Clear notes
        notesRepository.findByNotebookId(notebook.id).filter { it.sourceId != null }.forEach { notesRepository.deleteById(it.id!!) }
        return notes.map { notesRepository.save(Note(title = it.title, content = it.content, notebook = notebook, sourceId = it.id)) }
    }

    override fun getNotesFromNotebook(notebookId: String): List<Note> {
        return notesRepository.findByNotebookId(notebookId)
    }
}
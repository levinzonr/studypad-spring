package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.ModificationType
import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.repositories.NotesRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotesServiceImpl : NotesService {

    @Autowired
    private lateinit var notesRepository: NotesRepository

    @Autowired
    private lateinit var versioningService: VersioningService

    override fun createNote(title: String, content: String, notebook: Notebook): Note {
        val note =  notesRepository.save(Note(title = title, content = content, notebook = notebook))
        versioningService.modify(notebook.state, note, ModificationType.ADDED)
        return note
    }

    override fun getNote(id: Long): Note {
        return notesRepository.findById(id).orElseThrow {
            NotFoundException.Builder(Note::class).buildWithId(id.toString())
        }
    }

    override fun updateNote(id: Long, title: String?, content: String?): Note {
        val old = getNote(id)
        val newTitle = title ?: old.title
        val newContents = content ?: old.content

        return notesRepository.save(old.copy(title = newTitle, content = newContents))
    }

    override fun deleteNote(id: Long) {
        return notesRepository.deleteById(id)
    }

    override fun copyAndReplace(notes: List<Note>, notebook: Notebook): List<Note> {
        notebook.notes.forEach { notesRepository.deleteById(it.id!!) }
        return notes.map { notesRepository.save(Note(title = it.title, content = it.content, notebook = notebook)) }
    }
}
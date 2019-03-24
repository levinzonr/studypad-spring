package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.model.ModificationType
import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.payload.CreateNotePayload
import com.levinzonr.ezpad.domain.payload.UpdateNotePayload
import com.levinzonr.ezpad.domain.responses.NoteResponse
import com.levinzonr.ezpad.security.StudyPadUserDetails
import com.levinzonr.ezpad.services.NotebookService
import com.levinzonr.ezpad.services.NotesService
import com.levinzonr.ezpad.services.UserService
import com.levinzonr.ezpad.services.VersioningService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/notes")
class NotesController {

    @Autowired
    private lateinit var noteService: NotesService

    @Autowired
    private lateinit var notebookService: NotebookService

    @Autowired
    private lateinit var versioningService: VersioningService

    @Autowired
    private lateinit var userService: UserService

    @PostMapping
    fun createNewNote(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @Valid @RequestBody createNotePayload: CreateNotePayload) : NoteResponse {
        val user = userService.findUserById(userDetails.id)
        val nb = notebookService.getNotebookDetails(createNotePayload.notebookId)
        val note =  noteService.createNote(createNotePayload.title ?: "", createNotePayload.content ?: "", nb)
        versioningService.modify(nb.state, note, ModificationType.ADDED, user)
        return note.toResponse()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteNote(@PathVariable("id") id: Long) {
        val note = noteService.getNote(id)
        noteService.deleteNote(id)
        versioningService.modify(note.notebook.state, note, ModificationType.DELETED)
    }

    @PatchMapping("/{id}")
    fun updateNote(@PathVariable("id") id: Long, @Valid @RequestBody createNotePayload: UpdateNotePayload) : NoteResponse {
        val note = noteService.getNote(id)
        val updated =  noteService.updateNote(id, createNotePayload.title, createNotePayload.content)
        versioningService.modify(note.notebook.state, updated, ModificationType.UPDATED)
        return updated.toResponse()
    }

}
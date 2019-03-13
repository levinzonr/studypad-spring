package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.payload.CreateNotePayload
import com.levinzonr.ezpad.domain.payload.UpdateNotePayload
import com.levinzonr.ezpad.domain.responses.NoteResponse
import com.levinzonr.ezpad.services.NotebookService
import com.levinzonr.ezpad.services.NotesService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/notes")
class NotesController {

    @Autowired
    private lateinit var noteService: NotesService

    @Autowired
    private lateinit var notebookService: NotebookService

    @PostMapping
    fun createNewNote(@Valid @RequestBody createNotePayload: CreateNotePayload) : NoteResponse {
        val nb = notebookService.getNotebookDetails(createNotePayload.notebookId)
        return noteService.createNote(createNotePayload.title ?: "", createNotePayload.content ?: "", nb).toResponse()
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun deleteNote(@PathVariable("id") id: Long) {
        return noteService.deleteNote(id)
    }

    @PatchMapping("/{id}")
    fun updateNote(@PathVariable("id") id: Long, @Valid @RequestBody createNotePayload: UpdateNotePayload) : NoteResponse {
        return noteService.updateNote(id, createNotePayload.title, createNotePayload.content).toResponse()
    }

}
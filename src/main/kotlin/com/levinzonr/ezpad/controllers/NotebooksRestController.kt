package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.payload.ChangeNotebookPayload
import com.levinzonr.ezpad.domain.payload.CreateNotebookPayload
import com.levinzonr.ezpad.domain.responses.GradientColorResponse
import com.levinzonr.ezpad.domain.responses.NoteResponse
import com.levinzonr.ezpad.domain.responses.NotebookResponse
import com.levinzonr.ezpad.security.EzpadUserDetails
import com.levinzonr.ezpad.services.ColorsService
import com.levinzonr.ezpad.services.NotebookService
import com.levinzonr.ezpad.services.UserService
import com.levinzonr.ezpad.services.asString
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*
import javax.persistence.Temporal
import javax.validation.Valid

@RestController
@RequestMapping("/api/notebooks")
class NotebooksRestController {

    @Autowired
    private lateinit var notebooksService: NotebookService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var colorsService: ColorsService

    @GetMapping
    fun getCurrentUserNotebooks(@AuthenticationPrincipal details: EzpadUserDetails): List<NotebookResponse> {
        val user = userService.getUserById(details.userId)
        return notebooksService.getUserNotebooks(user).map { it.toResponse() }
    }


    @PostMapping
    fun postNewNotebook(@AuthenticationPrincipal details: EzpadUserDetails,
                        @Valid @RequestBody createNotebookPayload: CreateNotebookPayload) : NotebookResponse {
        val user = userService.getUserById(details.userId)
        return notebooksService.createNewNotebook(createNotebookPayload.name, user).toResponse()
    }


    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping("/{id}")
    fun deleteNotebook(@AuthenticationPrincipal details: EzpadUserDetails,
                       @PathVariable("id") id: Long) {
        val user = userService.getUserById(details.userId)
        notebooksService.deleteNotebook(id)
    }


    @PatchMapping("/{id}")
    fun updateNotebook(@AuthenticationPrincipal details: EzpadUserDetails,
                       @PathVariable("id") id: Long,
                       @RequestBody @Valid createNotebookPayload: ChangeNotebookPayload) : NotebookResponse {
        return notebooksService.updateNotebook(id, createNotebookPayload.name, createNotebookPayload.gradientColorResponse.asString()).toResponse()

    }

    @GetMapping("/{id}/notes")
    fun getNotesFromNotebook(@PathVariable("id") id : Long) : List<NoteResponse> {
        return notebooksService.getNotebookDetails(id).notes.map { it.toResponse() }
    }

    @GetMapping("/colors")
    fun getColors() : List<GradientColorResponse> {
        return colorsService.getColors()
    }

    @PostMapping("/import")
    fun importNotebook(@AuthenticationPrincipal userDetails: EzpadUserDetails, @RequestParam("id") id: String) : NotebookResponse {
        return notebooksService.createFromPublished(id, userDetails.userId).toResponse()
    }
}
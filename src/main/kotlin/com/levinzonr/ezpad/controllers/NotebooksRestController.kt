package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.model.VersionState
import com.levinzonr.ezpad.domain.payload.ChangeNotebookPayload
import com.levinzonr.ezpad.domain.payload.CreateNotebookPayload
import com.levinzonr.ezpad.domain.responses.GradientColorResponse
import com.levinzonr.ezpad.domain.responses.NoteResponse
import com.levinzonr.ezpad.domain.responses.NotebookResponse
import com.levinzonr.ezpad.security.StudyPadUserDetails
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
    fun getCurrentUserNotebooks(@AuthenticationPrincipal details: StudyPadUserDetails): List<NotebookResponse> {
        val user = userService.findUserById(details.userId) ?: throw NotFoundException.Builder(User::class).buildWithId(details.id)
        return notebooksService.getUserNotebooks(user).map { it.toResponse() }
    }


    @PostMapping
    fun postNewNotebook(@AuthenticationPrincipal details: StudyPadUserDetails,
                        @Valid @RequestBody createNotebookPayload: CreateNotebookPayload) : NotebookResponse {
        val user = userService.findUserById(details.userId) ?: throw NotFoundException.Builder(User::class).buildWithId(details.id)
        return notebooksService.createNewNotebook(createNotebookPayload.name, user, true).toResponse()
    }


    @ResponseStatus(code = HttpStatus.OK)
    @DeleteMapping("/{id}")
    fun deleteNotebook(@AuthenticationPrincipal details: StudyPadUserDetails,
                       @PathVariable("id") id: String) {
        val user = userService.findUserById(details.userId) ?: throw NotFoundException.Builder(User::class).buildWithId(details.id)
        notebooksService.deleteNotebook(id)
    }


    @PatchMapping("/{id}")
    fun updateNotebook(@AuthenticationPrincipal details: StudyPadUserDetails,
                       @PathVariable("id") id: String,
                       @RequestBody @Valid createNotebookPayload: ChangeNotebookPayload) : NotebookResponse {
        return notebooksService.updateNotebook(id, createNotebookPayload.name, createNotebookPayload.gradientColorResponse.asString()).toResponse()

    }

    @GetMapping("/{id}/notes")
    fun getNotesFromNotebook(@PathVariable("id") id : String) : List<NoteResponse> {
        return notebooksService.getNotebookDetails(id).notes.map { it.toResponse() }
    }

    @GetMapping("/colors")
    fun getColors() : List<GradientColorResponse> {
        return colorsService.getColors()
    }

    @PostMapping("/import")
    fun importNotebook(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestParam("id") id: String) : NotebookResponse {
        return notebooksService.createFromPublished(id, userDetails.userId).toResponse()
    }

    @PostMapping("/copy")
    fun importNotebookAsCopy(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestParam("id") id: String) : NotebookResponse {
        return notebooksService.createLocalCopy(id, userId = userDetails.userId).toResponse()
    }

    @GetMapping("/{id}/version")
    fun getVersionState(@PathVariable("id") id: String) : VersionState? {
        return notebooksService.getNotebookDetails(id).state
    }
}
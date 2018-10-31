package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.payload.CreateNotebookPayload
import com.levinzonr.ezpad.domain.responses.NotebookResponse
import com.levinzonr.ezpad.security.EzpadUserDetails
import com.levinzonr.ezpad.services.NotebookService
import com.levinzonr.ezpad.services.UserService
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

    @GetMapping
    fun getCurrentUserNotebooks(@AuthenticationPrincipal details: EzpadUserDetails): List<NotebookResponse> {
        val user = userService.getUserById(details.userId)
        return notebooksService.getUserNotebooks(user).map { it.toResponse() }
    }


    @PostMapping
    fun postNewNotebook(@AuthenticationPrincipal details: EzpadUserDetails,
                        @Valid @RequestBody createNotebookPayload: CreateNotebookPayload) : NotebookResponse {
        val user = userService.getUserById(details.userId)
        return notebooksService.createNewNotebook(createNotebookPayload.name, user, createNotebookPayload.color).toResponse()
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
                       @RequestBody @Valid createNotebookPayload: CreateNotebookPayload) : NotebookResponse {

        val user = userService.getUserById(id)
        return notebooksService.updateNotebook(id, createNotebookPayload.name, createNotebookPayload.color).toResponse()

    }
}
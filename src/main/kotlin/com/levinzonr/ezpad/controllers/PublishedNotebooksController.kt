package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.PublishedNotebook
import com.levinzonr.ezpad.domain.model.Topic
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.payload.PostSuggestionPayload
import com.levinzonr.ezpad.domain.payload.PublishedNotebookPayload
import com.levinzonr.ezpad.domain.responses.CommentResponse
import com.levinzonr.ezpad.domain.responses.ModificationResponse
import com.levinzonr.ezpad.domain.responses.PublishedNotebookDetail
import com.levinzonr.ezpad.domain.responses.PublishedNotebookResponse
import com.levinzonr.ezpad.security.StudyPadUserDetails
import com.levinzonr.ezpad.services.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.jpa.repository.Query
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/shared")
class PublishedNotebooksController {



    @Autowired
    private lateinit var service: PublishedNotebookService

    @Autowired
    private lateinit var notebookService: NotebookService

    @Autowired
    private lateinit var commentService: CommentService

    @Autowired
    private lateinit var tagService: TagService

    @Autowired
    private lateinit var userService: UserService


    @GetMapping
    fun getRelevant(@AuthenticationPrincipal user: StudyPadUserDetails) : List<PublishedNotebookResponse> {
        return service.getMostRelevant().map { it.toResponse(user) }
    }

    @PostMapping
    fun publishNotebook(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestBody notebook: PublishedNotebookPayload) : PublishedNotebookResponse {
        return service.publishNotebook(
                userDetails.userId, notebook.notebookId, notebook.languageCode, notebook.title,
                notebook.description, notebook.topic, notebook.tags ?: setOf(), notebook.universityId).toResponse(userDetails)
    }

    @GetMapping("/{id}")
    fun getPublishedNotebookDetails(@AuthenticationPrincipal details: StudyPadUserDetails, @PathVariable("id") id: String) : PublishedNotebookDetail {
        return service.getPublishedNotebookById(id).toDetailedResponse(details)
    }

    @PostMapping("/quick")
    fun quickShare(@AuthenticationPrincipal userDetails: StudyPadUserDetails, @RequestParam("id") notebookId: String) : PublishedNotebookResponse {
        return service.quickPublish(userId = userDetails.userId, notebookId = notebookId).toResponse(userDetails)
    }


    @PostMapping("/{id}/comment")
    fun postNotebookComment(@AuthenticationPrincipal user: StudyPadUserDetails,
                            @RequestParam comment: String,
                            @PathVariable("id") id: String) : CommentResponse {
        return commentService.postNotebookComment(user.userId, id, comment).toResponse()
    }


    @DeleteMapping("/comments/{id}")
    @ResponseStatus(code = HttpStatus.OK)
    fun deleteComment(@PathVariable("id") id: Long) {
        commentService.deleteComment(id)
    }


    @PostMapping("/comments/{id}")
    fun updateComment(@PathVariable("id") id: Long, @RequestParam comment: String) : CommentResponse {
        return commentService.updateComment(id, comment).toResponse()
    }

    @GetMapping("/find")
    fun findNotebooks(@AuthenticationPrincipal user: StudyPadUserDetails, @RequestParam(value = "topic") topic: String, @RequestParam("tags") tags: Set<String>) : List<PublishedNotebookResponse> {
        return service.findNotebooks(tags, topic).map { it.toResponse(user) }
    }

    @GetMapping("/tags")
    fun getTags(@RequestParam("name") name: String) : List<String> {
        return tagService.findTagsByName(query = name).map { it.name }
    }


    @PostMapping("/{id}/suggestions")
    @ResponseStatus(HttpStatus.CREATED)
    fun createSuggestion(@AuthenticationPrincipal user: StudyPadUserDetails, @PathVariable("id") notebookId: String, @RequestBody postSuggestionPayload: PostSuggestionPayload) {
        val user = userService.findUserById(user.id) ?: throw NotFoundException.Builder(User::class).buildWithId(user.id)
        service.createSuggestion(user, postSuggestionPayload, notebookId)
    }

    @GetMapping("/{id}/suggestions")
    fun getSuggestions(@PathVariable("id") notebookId: String) : List<ModificationResponse> {
        return service.getPublishedNotebookById(notebookId).state?.modifications?.map { it.toResponse() } ?: listOf()
    }

    @PostMapping("{id}/suggestions/approve")
    fun approveSuggestions(
            @AuthenticationPrincipal user: StudyPadUserDetails,
            @PathVariable("id") notebookId: String,
            @RequestParam("ids") ids: List<Long>) : PublishedNotebookDetail {

        service.approveModifications(notebookId, notebookId, ids)
        return service.getPublishedNotebookById(notebookId).toDetailedResponse(user)
    }

    @PatchMapping("{id}/suggestions/local")
    fun applyLocalChanges(
            @AuthenticationPrincipal user: StudyPadUserDetails,
            @PathVariable("id") notebookId: String) : PublishedNotebookDetail {
        return service.applyLocalAuthorChanges(user.userId, notebookId).toDetailedResponse(user)
    }


}
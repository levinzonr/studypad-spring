package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.InvalidPayloadException
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.*
import com.levinzonr.ezpad.domain.payload.PostSuggestionPayload
import com.levinzonr.ezpad.domain.repositories.PublishedNoteRepository
import com.levinzonr.ezpad.domain.repositories.PublishedNotebookRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class PublishedNotebookServiceImpl : PublishedNotebookService {


    @Autowired
    private lateinit var sharedNotesRepo : PublishedNoteRepository

    @Autowired
    private lateinit var sharedNotebookRepo: PublishedNotebookRepository

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var notebookService: NotebookService

    @Autowired
    private lateinit var universityService: UniversityService

    @Autowired
    private lateinit var tagService: TagService

    @Autowired
    private lateinit var topicService: TopicService

    @Autowired
    private lateinit var notesService: NotesService

    @Autowired
    private lateinit var versioningService: VersioningService

    override fun publishNotebook(userId: String, notebookId: String, languageCode: String?, title: String?, description: String?, topicId: Long?, tags: Set<String>, universityID: Long?): PublishedNotebook {
        val author = userService.findUserById(userId) ?: throw NotFoundException.Builder(User::class).buildWithId(userId)
        val notebook = notebookService.getNotebookDetails(notebookId)

        // Check if notebook has been already published
        if (notebook.publishedVersionId != null) {
            throw InvalidPayloadException()
        } else {


            val uni: University? = universityID?.let { universityService.findById(it) }
            val topic: Topic? = topicId?.let { topicService.findById(topicId) }

            val domainTags = tags.map { tagService.createTag(it) }.toSet()

            val published = sharedNotebookRepo.save(PublishedNotebook(
                    author = author,
                    lastUpdatedTimestamp = Date().time,
                    createdTimestamp = Date().time,
                    description = description,
                    title = title ?: notebook.name,
                    university = uni,
                    languageCode = languageCode,
                    topic = topic,
                    tags = domainTags
            ))

            // Update 1 to 1 association
            notebookService.updateNotebook(notebook.copy(publishedVersionId = published.id))

            val state = versioningService.initPublishedVersion(published)
            notebookService.updateState(notebook, versioningService.initLocalVersion(published, notebook))
            published.state = state

            notesService.copyAndReplace(notebook.notes, published)


            return sharedNotebookRepo.save(published)
        }
    }


    override fun quickPublish(userId: String, notebookId: String) : PublishedNotebook {
        val author = userService.findUserById(userId) ?: throw NotFoundException.Builder(User::class).buildWithId(userId)
        val notebook = notebookService.getNotebookDetails(notebookId)

        // Check if notebook has been already published
        if (notebook.publishedVersionId != null) {
            throw InvalidPayloadException()
        } else {


            val published = sharedNotebookRepo.save(PublishedNotebook(
                    author = author,
                    excludeFromSearch = true,
                    title = notebook.name,
                    lastUpdatedTimestamp = Date().time,
                    createdTimestamp = Date().time,
                    university = null,
                    tags = setOf()
            ))

            // Update 1 to 1 association
            notebookService.updateNotebook(notebook.copy(publishedVersionId = published.id))

            published.state = versioningService.initPublishedVersion(published)


            notebookService.updateState(notebook, versioningService.initLocalVersion(published, notebook))

            notesService.copyAndReplace(notebook.notes, published)
            return sharedNotebookRepo.save(published)
        }
    }

    override fun createSuggestion(user: User, postSuggestionPayload: PostSuggestionPayload, notebookId: String) {
        val notebook = getPublishedNotebookById(notebookId)
        if (postSuggestionPayload.noteId == null) {
            versioningService.modify(notebook.state, Note(title = postSuggestionPayload.newTitle, content = postSuggestionPayload.newContent, notebook = notebook), ModificationType.ADDED)
        } else {
            val note = notesService.getNote(postSuggestionPayload.noteId)
            if (postSuggestionPayload.newContent == null && postSuggestionPayload.newTitle == null) {
                versioningService.modify(notebook.state, note, ModificationType.DELETED)
            } else {
                versioningService.modify(notebook.state, note.copy(title = postSuggestionPayload.newTitle, content = postSuggestionPayload.newContent), ModificationType.DELETED)
            }
        }
    }

    override fun filterByTag(tag: String): List<PublishedNotebook> {
        return sharedNotebookRepo.findAll().filter {
            it.tags.any { it.name.contains(tag, true) }
        }
    }

    override fun filterByTopic(topic: String) : List<PublishedNotebook> {
        return sharedNotebookRepo.findAll().filter { it.topic?.name.toString().contains(topic, true) }
    }

    override fun getMostRelevant(): List<PublishedNotebook> {
        return sharedNotebookRepo.findAll().sortedByDescending { it.lastUpdatedTimestamp }
    }


    override fun findNotebooks(tags: Set<String>, topic: String): List<PublishedNotebook> {
        return tags.flatMap { filterByTag(it) }
                .union(filterByTopic(topic))
                .toList()
                .distinctBy { it.id }

    }

    override fun getPublishedNotebookById(id: String): PublishedNotebook {
        return sharedNotebookRepo.findById(id).orElseThrow { NotFoundException.Builder(PublishedNotebook::class).buildWithId(id) }
    }
}
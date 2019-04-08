package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.BadRequestException
import com.levinzonr.ezpad.domain.errors.InvalidPayloadException
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.*
import com.levinzonr.ezpad.domain.payload.PostSuggestionPayload
import com.levinzonr.ezpad.domain.repositories.PublishedNotebookRepository
import com.levinzonr.ezpad.domain.responses.SectionResponse
import com.levinzonr.ezpad.utils.first
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class PublishedNotebookServiceImpl : PublishedNotebookService {

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
        val author = userService.findUserById(userId)
                ?: throw NotFoundException.Builder(User::class).buildWithId(userId)
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

            notesService.exportNotes(notebook.notes, published)


            return sharedNotebookRepo.save(published)
        }
    }


    override fun quickPublish(userId: String, notebookId: String): PublishedNotebook {
        val author = userService.findUserById(userId)
                ?: throw NotFoundException.Builder(User::class).buildWithId(userId)
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

            notesService.exportNotes(notebook.notes, published)
            return sharedNotebookRepo.save(published)
        }
    }

    override fun createSuggestion(user: User, postSuggestionPayload: PostSuggestionPayload, notebookId: String) {
        val notebook = getPublishedNotebookById(notebookId)
        val id = postSuggestionPayload.noteId
        val sourcedNote = NoteBody(title = postSuggestionPayload.newTitle, content = postSuggestionPayload.newContent, id = id, sourceId = id)
        versioningService.modify(notebook.state, sourcedNote, postSuggestionPayload.type.modType, user)
    }

    override fun filterByTag(tag: String): List<PublishedNotebook> {
        return sharedNotebookRepo.findAll().filter {
            it.tags.any { it.name.contains(tag, true) }
        }
    }

    override fun filterByTopic(topic: String): List<PublishedNotebook> {
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

    override fun approveModifications(userId: String, id: String, modificationIds: List<Long>): PublishedNotebook {
        val shared = getPublishedNotebookById(id).also { it.checkWritePolicy(userId) }
        versioningService.getModificationsByIds(modificationIds)
                .map { mod ->
                    println("Mode $mod")
                    when (mod) {
                        is Modification.Deleted -> {
                            notesService.deleteNote(mod.noteId!!)
                            null
                        }
                        is Modification.Updated -> {
                            notesService.updateNote(mod.noteId!!, mod.title, mod.content)
                        }
                        is Modification.Added -> {
                            val note = notesService.createNote(mod.title, mod.content, shared)
                            mod.noteId?.let { notesService.updateNote(mod.noteId, sourceId = note.id) }
                        }
                    }
                }

        shared.notes = notesService.getNotesFromNotebook(id)
        shared.state = versioningService.applyModifications(shared.state, modificationIds)
        println(shared.notes.first())
        return sharedNotebookRepo.save(shared)
    }

    override fun migrateToSuggestions(user: User, notebookId: String): PublishedNotebook {
        val shared = getPublishedNotebookById(notebookId)
        val notebook = notebookService.getUserNotebooks(user).firstOrNull { it.publishedVersionId == notebookId }
                ?: throw NotFoundException.buildWithId<Notebook>(notebookId)
        val localModifications = notebook.state?.modifications ?: listOf()
        val payloads = localModifications.map {
            when (it) {
                is Modification.Added -> PostSuggestionPayload(noteId =  it.id, newContent = it.content, newTitle = it.title,  type = ModificationType.ADDED.toRepsonse())
                is Modification.Updated -> PostSuggestionPayload(noteId = it.noteId, newTitle = it.title, newContent = it.content,  type = ModificationType.UPDATED.toRepsonse())
                is Modification.Deleted -> PostSuggestionPayload(noteId = it.noteId, type = ModificationType.DELETED.toRepsonse())
            }
        }
        payloads.forEach { createSuggestion(user, it, notebookId) }
        notebookService.updateState(notebook,versioningService.initLocalVersion(shared, notebook))
        return getPublishedNotebookById(notebookId)
    }

    override fun applyLocalAuthorChanges(user: User, id: String): PublishedNotebook {
        val shared = getPublishedNotebookById(id).also { it.checkWritePolicy(user) }
        val notebook = notebookService.getUserNotebooks(user).firstOrNull { it.publishedVersionId == id }
                ?: throw NotFoundException.buildWithId<Notebook>(id)
        val modifications = notebook.state?.modifications ?: listOf()
        if (modifications.isEmpty()) throw BadRequestException("No modifications to apply")
        return approveModifications(user.id!!, id, modifications.map { it.id!! }).also {
            notebookService.updateState(notebook, versioningService.initLocalVersion(shared, notebook))

        }
    }


    override fun handleChangesMigration(user: User, notebookId: String): PublishedNotebook {
        val notebook = getPublishedNotebookById(notebookId)
        return if (user.id == notebook.author.id) applyLocalAuthorChanges(user, notebookId) else migrateToSuggestions(user, notebookId)
    }


    override fun getAll(): List<PublishedNotebook> {
        return sharedNotebookRepo.findAll().filterNot { it.excludeFromSearch }
    }

    override fun getFeed(user: User): List<Section> {

        val secionSize = 10

        val recent = getAll().sortedByDescending { it.updatedAt.time }.first(secionSize)
        val fromUniversity = if (user.university != null ) getAll().filter { it.university?.id == user.university.id }.first(secionSize) else listOf()
        val popular = getAll().sortedByDescending { it.comments.count() }.first(secionSize)

        return listOf(
                Section("From My University", fromUniversity),
                Section("Recently Updated", recent),
                Section("Popular", popular))
    }
}
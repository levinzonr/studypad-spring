package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.PublishedNote
import com.levinzonr.ezpad.domain.model.PublishedNotebook
import com.levinzonr.ezpad.domain.model.Topic
import com.levinzonr.ezpad.domain.model.University
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

    override fun publishNotebook(userId: Long, notebookId: Long, title: String?, description: String?, topicId: Long?, tags: Set<String>, universityID: Long?): PublishedNotebook {
        val author = userService.getUserById(userId)
        val notebook = notebookService.getNotebookDetails(notebookId)
        val uni : University?  = universityID?.let { universityService.findById(it) }
        val topic : Topic? = topicId?.let { topicService.findById(topicId) }

        val domainTags = tags.map { tagService.createTag(it) }.toSet()

        val published = sharedNotebookRepo.save(PublishedNotebook(
                author = author,
                lastUpdatedTimestamp = Date().time,
                createdTimestamp = Date().time,
                title = title ?: notebook.name,
                university = uni,
                topic = topic,
                tags = domainTags,
                source = notebook
        ))

        notebook.notes.forEach { sharedNotesRepo.save(
                PublishedNote(title = it.title,
                        content = it.content,
                        notebook = published)
        ) }

        return published
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
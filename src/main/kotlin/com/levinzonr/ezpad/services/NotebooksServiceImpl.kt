package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.NotebooksRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class NotebooksServiceImpl : NotebookService {

    @Autowired
    private lateinit var repository: NotebooksRepository

    @Autowired
    private lateinit var publishedRepo: PublishedNotebookService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var colorService: ColorsService

    @Autowired
    private lateinit var notesService: NotesService


    override fun getUserNotebooks(user: User): List<Notebook> {
        return repository.findByUser(user)
    }

    override fun createNewNotebook(name: String, user: User): Notebook {
        val color = colorService.getRandomColor()
        return repository.save(Notebook(name = name, user = user, colour = color))
    }

    override fun getNotebookDetails(id: Long): Notebook {
        return repository.findById(id).orElseThrow {
            NotFoundException
                    .Builder(Notebook::class)
                    .buildWithId(id.toString())
        }
    }

    override fun updateNotebook(id: Long, name: String?, color: String?): Notebook {
        val old = getNotebookDetails(id)
        val newName = name ?: old.name
        val newColor = color ?: old.colour
        return repository.save(old.copy(name = newName, colour = newColor))
    }

    override fun deleteNotebook(id: Long) {
        repository.deleteById(id)
    }

    override fun updateNotebook(notebook: Notebook): Notebook {
        return repository.save(notebook)
    }

    override fun createFromPublished(publishedId: String, userId: String): Notebook {
        val user = userService.findUserById(userId) ?: throw NotFoundException.Builder(User::class).buildWithId(userId)
        val published = publishedRepo.getPublishedNotebookById(publishedId)

        val previouslyImported = getUserNotebooks(user).firstOrNull { it.sourceId == publishedId }

        // User is not an author and didn't import it previously
        if (user.id != published.author.id && previouslyImported == null) {
            val notebook = createNewNotebook(published.title, user)
            published.notes.forEach { notesService.createNote(it.title ?: "Unknown title", it.content ?: "", notebook) }
            repository.save(notebook.copy(sourceId = publishedId))
            return notebook
            // User did import this notebook previously, all notes will be overriden
        } else if (previouslyImported != null) {
            previouslyImported.notes.map { notesService.deleteNote(it.id!!) }
            val notes = published.notes.map {
                notesService.createNote(it.title ?: "Unknown title", it.content ?: "", previouslyImported)
            }
            return repository.save(previouslyImported.copy(notes = notes))

            // TODO User is the author
        } else {
            val notebook = createNewNotebook(published.title, user)
            published.notes.forEach { notesService.createNote(it.title ?: "", it.content ?: "", notebook) }
            return notebook
        }

    }
}
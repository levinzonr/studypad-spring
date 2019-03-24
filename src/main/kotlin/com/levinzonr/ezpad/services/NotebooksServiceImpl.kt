package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.model.VersionState
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

    @Autowired
    private lateinit var versioningService: VersioningService


    override fun getUserNotebooks(user: User): List<Notebook> {
        return repository.findByAuthor(user)
    }

    override fun createNewNotebook(name: String, user: User): Notebook {
        val color = colorService.getRandomColor()
        return repository.save(Notebook(name = name, user = user, colour = color))
    }

    override fun getNotebookDetails(id: String): Notebook {
        return repository.findById(id).orElseThrow {
            NotFoundException
                    .Builder(Notebook::class)
                    .buildWithId(id)
        }
    }

    override fun updateNotebook(id: String, name: String?, color: String?): Notebook {
        val old = getNotebookDetails(id)
        val newName = name ?: old.name
        val newColor = color ?: old.colour
        return repository.save(old.copy(name = newName, colour = newColor))
    }

    override fun deleteNotebook(id: String) {
        repository.deleteById(id)
    }

    override fun updateNotebook(notebook: Notebook): Notebook {
        return repository.save(notebook)
    }

    override fun createFromPublished(publishedId: String, userId: String): Notebook {
        val user = userService.findUserById(userId) ?: throw NotFoundException.Builder(User::class).buildWithId(userId)
        val published = publishedRepo.getPublishedNotebookById(publishedId)

        val previouslyImported = getUserNotebooks(user).firstOrNull { it.publishedVersionId == publishedId  }

        if (previouslyImported == null) {
            val notebook = createNewNotebook(published.title, user)
            val state = versioningService.initLocalVersion(published, notebook)
            val notes = notesService.importNotes(published.notes, notebook)
            return repository.save(notebook.copy(state = state, publishedVersionId = publishedId, notes = notes))

        } else {
            // Already improted, replace all notes and reset state
            val state = versioningService.initLocalVersion(published, previouslyImported)
            val notes = notesService.importNotes(published.notes, previouslyImported)
            return repository.save(previouslyImported.copy(state = state, notes = notes))
        }

    }

    override fun updateState(notebook: Notebook, versionState: VersionState) {
        repository.save(notebook.copy(state = versionState))
    }
}
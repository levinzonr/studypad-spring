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


    override fun getUserNotebooks(user: User): List<Notebook> {
        return repository.findByUser(user)
    }

    override fun createNewNotebook(name: String, user: User, color: String): Notebook {
       return repository.save(Notebook(name = name, user = user, colour = color))
    }

    override fun getNotebookDetails(id: Long): Notebook {
        return repository.findById(id).orElseThrow {
            NotFoundException
                    .Builder(Notebook::class)
                    .buildWithId(id.toString())
        }
    }

    override fun updateNotebook(id: Long, name: String?, color: String? ) : Notebook {
        val old = getNotebookDetails(id)
        val newName = name ?: old.name
        val newColor = color ?: old.colour
        return repository.save(old.copy(name = newName, colour = newColor))
    }

    override fun deleteNotebook(id: Long) {
        repository.deleteById(id)
    }
}
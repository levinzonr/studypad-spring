package com.levinzonr.ezpad.repositories

import com.levinzonr.ezpad.domain.model.Note
import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.NotebooksRepository
import com.levinzonr.ezpad.domain.repositories.NotesRepository
import com.levinzonr.ezpad.domain.repositories.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class NotesRepositoryTest {

    @Autowired
    lateinit var userRepo: UserRepository


    @Autowired
    private lateinit var notebookRepository: NotebooksRepository

    @Autowired
    private lateinit var notesRepo: NotesRepository

    @Test
    fun testCrud() {
        val user = userRepo.save(User(email = "email", password = "password"))
        val notebooks = notebookRepository.save(Notebook(name = "Test", user = user))

        repeat(10) {
            val note = notesRepo.save(Note(title = "Title $it", notebook = notebooks))
        }

        assert(notesRepo.findByNotebook(notebooks).count() == 10)

    }
}
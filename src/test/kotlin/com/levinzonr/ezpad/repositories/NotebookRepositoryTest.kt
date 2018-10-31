package com.levinzonr.ezpad.repositories

import com.levinzonr.ezpad.domain.model.Notebook
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.NotebooksRepository
import com.levinzonr.ezpad.domain.repositories.UserRepository
import com.levinzonr.ezpad.services.UserService
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.repository.CrudRepository
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class NotebookRepositoryTest {

    @Autowired
    lateinit var userRepo: UserRepository


    @Autowired
    private lateinit var notebookRepository: NotebooksRepository


    @Test
    fun testCrud() {

        val user = userRepo.save(User(email = "email", password = "password"))

        val emptyList = notebookRepository.findAll()
        assert(emptyList.count() == 0)

        var notebook : Notebook? = null
        repeat(10) {
            notebook = notebookRepository.save(
                    Notebook(name = "Test nb $it",user = user ))
            println("Notebook saved: $notebook")
        }

        assert(notebookRepository.findAll().count() == 10)

        notebookRepository.delete(notebook!!)
        assert(notebookRepository.findAll().count() == 9)
        assert(!notebookRepository.findAll().contains(notebook))

    }

    @Test
    fun testUserNotebooks() {

      val users = arrayListOf<User>()
        repeat(10) {
            val user = userRepo.save(User(email = "email $it", password = "password"))
            users.add(user)
        }

        val empty = notebookRepository.findByUser(users.first())
        assert(empty.isEmpty())

        val notebookOfFirstUser = notebookRepository.save( Notebook(user = users.first(), name =  "Notebook"))

        val one = notebookRepository.findByUser(users.first())
        assert(!one.isEmpty())
        assert(one.contains(notebookOfFirstUser))

        assert(notebookRepository.findByUser(users.last()).isEmpty())


        val oneById = notebookRepository.findByUserId(users.first().id!!)
        assert(!oneById.isEmpty())

    }

}
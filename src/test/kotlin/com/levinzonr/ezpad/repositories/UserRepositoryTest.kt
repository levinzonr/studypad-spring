package com.levinzonr.ezpad.repositories

import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.UserRepository
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@DataJpaTest
class UserRepositoryTest {

    @Autowired
    lateinit var repositoryTest: UserRepository

    @Test
    fun testEmailExist() {
        val user = User(email = "roma@mail.ru", password = "1996")
        repositoryTest.save(user)

        val found = repositoryTest.findByEmail("roma@mail.ru")
        assert(found != null)

        val notFound = repositoryTest.findByEmail("roma@maial.ru")
        assert(notFound == null)
    }

}
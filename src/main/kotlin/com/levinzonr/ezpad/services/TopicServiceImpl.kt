package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.Topic
import com.levinzonr.ezpad.domain.repositories.TopicRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TopicServiceImpl : TopicService {

    @Autowired
    private lateinit var repo: TopicRepository

    override fun createTopic(name: String) : Topic {
        return repo.save(Topic(name = name))
    }

    override fun getTopics(): List<Topic> {
        return repo.findAll().toList()
    }

    override fun findById(id: Long): Topic {
        return repo.findById(id).orElseThrow { NotFoundException.Builder(Topic::class).buildWithId(id.toString()) }
    }
}
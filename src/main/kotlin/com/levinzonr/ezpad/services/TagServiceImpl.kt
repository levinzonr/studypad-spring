package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Tag
import com.levinzonr.ezpad.domain.repositories.TagRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class TagServiceImpl : TagService {

    @Autowired
    private lateinit var repo: TagRepository

    override fun createTag(name: String): Tag {
        return repo.save(Tag(name.toLowerCase()))
    }

    override fun findTagsByName(query: String): List<Tag> {
        return repo.findAll().toList()
                .filter { it.name.contains(query, true) }
    }
}
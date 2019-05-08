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
        return repo.save(Tag(name.toLowerCase().trim()))
    }

    override fun findTagsByName(query: String): List<Tag> {
        val all = repo.findAll().toList()
        println(all)
        val filtered = all
                .also { it.forEach { println("${it.name} contains $query == ${it.name.contains(query, true)}")  }}
                .filter { it.name.contains(query, true) }.sortedBy { it.name }
        println(filtered)
        return filtered
    }

    override fun init() {

    }
}
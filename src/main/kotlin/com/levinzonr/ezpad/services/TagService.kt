package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Tag

interface TagService {

    fun createTag(name: String) : Tag

    fun findTagsByName(query: String) : List<Tag>

    fun init()

}
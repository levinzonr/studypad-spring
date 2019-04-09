package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Topic

interface TopicService {

    fun createTopic(name: String) : Topic

    fun getTopics() : List<Topic>

    fun findById(id: Long): Topic

    fun findByIdOrNull(id: Long) : Topic?

    fun editTopic(id: Long, name: String?) : Topic

    fun deleteTopic(id: Long)

    fun init()

}
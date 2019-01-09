package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.model.Topic
import com.levinzonr.ezpad.domain.payload.TopicPayload
import com.levinzonr.ezpad.services.TopicService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("api/config")
class ConfigurationController {


    @Autowired
    private lateinit var topicService: TopicService

    @Autowired
    private lateinit var tagService: TopicService


    @PostMapping("/topics")
    fun createTopic(@RequestBody topicPayload: TopicPayload) : Topic {
        return topicService.createTopic(topicPayload.name)
    }

    @DeleteMapping("/topics/{id}")
    fun deleteTopic(@PathVariable("id") id: Long) {
        return topicService.deleteTopic(id)
    }

    @PatchMapping("/topics/{id}")
    fun updateTopic(@PathVariable("id") id: Long, @RequestBody topicPayload: TopicPayload) : Topic {
        return topicService.editTopic(id, topicPayload.name)
    }

}
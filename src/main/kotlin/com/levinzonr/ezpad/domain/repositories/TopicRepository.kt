package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.Topic
import org.springframework.data.repository.CrudRepository

interface TopicRepository : CrudRepository<Topic, Long> {
}
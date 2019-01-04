package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.Tag
import org.springframework.data.repository.CrudRepository

interface TagRepository : CrudRepository<Tag, String> {
}
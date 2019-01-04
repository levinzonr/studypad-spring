package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository : CrudRepository<Comment, Long> {
}
package com.levinzonr.ezpad.domain.responses

class CommentResponse(
        val author: UserResponse,
        val content: String,
        val id: Long,
        val dateCreated: Long
)
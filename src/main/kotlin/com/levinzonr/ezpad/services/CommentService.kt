package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Comment

interface CommentService  {

    fun postNotebookComment(userId: Long, notebookId: String, comment: String) : Comment

    fun deleteComment(commentId: Long)

    fun findComment(commentId: Long) : Comment

    fun updateComment(commentId: Long, comment: String) : Comment

}
package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.Comment
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.CommentRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommentServiceImpl : CommentService {


    @Autowired
    private lateinit var messageService: MessageService

    @Autowired
    private lateinit var repo: CommentRepository

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var notebookService: PublishedNotebookService


    override fun postNotebookComment(userId: String, notebookId: String, comment: String): Comment {
        val user = userService.findUserById(userId) ?: throw NotFoundException.Builder(User::class).buildWithId(userId)
        val notebook = notebookService.getPublishedNotebookById(notebookId)
        return repo.save(Comment(
                author = user,
                content = comment,
                notebook = notebook)).also { messageService.notifyOnComment(notebook, user) }
    }

    override fun deleteComment(commentId: Long) {
        repo.delete(findComment(commentId))
    }

    override fun findComment(commentId: Long): Comment {
        return repo.findById(commentId).orElseThrow { NotFoundException.Builder(Comment::class).buildWithId(commentId.toString()) }
    }

    override fun updateComment(commentId: Long, comment: String): Comment {
        val oldComment = findComment(commentId)
        return repo.save(oldComment.copy(content = comment, edited = true))
    }
}
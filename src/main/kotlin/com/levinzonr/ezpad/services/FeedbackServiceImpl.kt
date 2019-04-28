package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.Feedback
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.payload.UserFeedbackPayload
import com.levinzonr.ezpad.domain.responses.FeedbackRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FeedbackServiceImpl : FeedbackService {

    @Autowired
    private lateinit var repository: FeedbackRepository

    @Autowired
    private lateinit var userService: UserService

    override fun saveFeedback(userId: String, payload: UserFeedbackPayload) {
        val user =  userService.findUserById(userId) ?: throw NotFoundException.buildWithId<User>(userId)
        val feedback = repository.save(Feedback(
                user = user,
                appVersionName = payload.appVersionName,
                appVersionCode = payload.appVersionCode,
                androidVersion = payload.androidVersion,
                device = payload.device,
                feedback = payload.feedback))
    }

    override fun getAll(): List<Feedback> {
        return repository.findAll().toList()
    }
}
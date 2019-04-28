package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.Feedback
import com.levinzonr.ezpad.domain.payload.UserFeedbackPayload

interface FeedbackService {

    fun saveFeedback(userId: String, payload: UserFeedbackPayload)

    fun getAll() : List<Feedback>

}
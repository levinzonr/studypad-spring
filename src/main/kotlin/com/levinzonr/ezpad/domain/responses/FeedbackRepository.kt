package com.levinzonr.ezpad.domain.responses

import com.levinzonr.ezpad.domain.model.Feedback
import org.springframework.data.repository.CrudRepository

interface FeedbackRepository : CrudRepository<Feedback, Long>
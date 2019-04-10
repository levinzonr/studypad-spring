package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.NotificationPayload
import org.springframework.data.repository.CrudRepository

interface NotificationRepository : CrudRepository<NotificationPayload, Long>
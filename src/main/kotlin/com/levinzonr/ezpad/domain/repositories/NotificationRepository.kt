package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.Notification
import org.springframework.data.repository.CrudRepository

interface NotificationRepository : CrudRepository<Notification, Long>
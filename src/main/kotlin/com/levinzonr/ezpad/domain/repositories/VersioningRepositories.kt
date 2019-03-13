package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.Modification
import com.levinzonr.ezpad.domain.model.VersionState
import org.springframework.data.repository.CrudRepository

interface VersionStateRepository: CrudRepository<VersionState, Long>

interface ModificationRepository: CrudRepository<Modification, Long>
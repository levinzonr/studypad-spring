package com.levinzonr.ezpad.domain.repositories

import com.levinzonr.ezpad.domain.model.University
import org.springframework.data.repository.CrudRepository

interface UniversityRepository : CrudRepository<University, Long> {

}
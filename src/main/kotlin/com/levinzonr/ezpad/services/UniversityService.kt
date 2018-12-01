package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.model.University
import com.levinzonr.ezpad.domain.model.User
import org.springframework.stereotype.Service

@Service
interface UniversityService {

    fun findUniversities(query: String) : List<University>

    fun createUniversity(fullName: String, shortName: String, aliases: List<String>? = null) : University

    fun getStudentsFromUniversity(uniId: Long) : List<User>

    fun findById(uniId: Long) : University

    fun updateAliases(id: Long, aliases: List<String>) : University

    fun findAll() : List<University>

    fun init()

}
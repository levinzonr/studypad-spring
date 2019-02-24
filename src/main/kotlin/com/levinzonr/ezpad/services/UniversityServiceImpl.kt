package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.University
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.UniversityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.io.File
import java.io.FileInputStream
import java.nio.charset.Charset

@Service
class UniversityServiceImpl : UniversityService {

    @Autowired
    private lateinit var universityRepository: UniversityRepository

    override fun findUniversities(query: String): List<University> {
        val all = universityRepository.findAll()
        if (query.isEmpty()) return all.toList()

        return listOf<List<University>>(
                all.filter { it.fullName.contains(query, true) }.toList(),
                all.filter { it.shortName.contains(query, true) }.toList(),
                all.filter { it.aliases().any {it.contains(query, true)} }
        ).flatten().distinctBy { it.id }

    }

    override fun createUniversity(fullName: String, shortName: String, aliases: List<String>?) : University {
        val university = University(fullName = fullName, shortName = shortName, aliases = aliases?.toAliases() ?: "")
        return universityRepository.save(university)
    }

    override fun deleteUniversity(id: Long) {
        universityRepository.delete(findById(id))
    }

    override fun updateUniversity(id: Long, newFullName: String?, newShortName: String?): University {
        val uni = findById(id)
        val newUni = uni.copy(
                fullName = newFullName ?: uni.fullName,
                shortName = newShortName ?: uni.shortName
        )
        return universityRepository.save(newUni)
    }

    override fun getStudentsFromUniversity(uniId: Long): List<User> {
        return findById(uniId).students.toList()
    }

    override fun updateAliases(id: Long, aliases: List<String>): University {
        val uni = findById(id)
        val newUni = uni.copy(aliases = aliases.toAliases())
        universityRepository.save(newUni)
        return newUni
    }

    override fun findById(uniId: Long): University {
        return universityRepository.findById(uniId)
                .orElseThrow {
                    NotFoundException.Builder(University::class)
                            .buildWithId(uniId.toString())
                }

    }

    override fun findAll(): List<University> {
        return universityRepository.findAll().toList()
    }

    override fun init() {
        universityRepository.deleteAll()
        val json = File("src/main/resources/source_unis.json").inputStream().readBytes().toString(Charsets.UTF_8)
    }


    fun List<String>.toAliases() : String {
        return joinToString(";")
    }

}
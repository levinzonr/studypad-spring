package com.levinzonr.ezpad.services

import com.google.gson.Gson
import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.ExportedUniversity
import com.levinzonr.ezpad.domain.model.University
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.UniversityRepository
import com.levinzonr.ezpad.utils.first
import com.levinzonr.ezpad.utils.fromJsonFile
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
                all.filter { it.aliases().any {it.contains(query, true)} }
        ).flatten().distinctBy { it.id }
                .first(30)

    }

    override fun createUniversity(fullName: String, shortName: String, aliases: List<String>?) : University {
        val university = University(fullName = fullName, country = shortName, aliases = aliases?.toAliases() ?: "", countryCode = "")
        return universityRepository.save(university)
    }

    override fun deleteUniversity(id: Long) {
        universityRepository.delete(findById(id))
    }

    override fun updateUniversity(id: Long, newFullName: String?, newShortName: String?): University {
        val uni = findById(id)
        val newUni = uni.copy(
                fullName = newFullName ?: uni.fullName
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


    override fun findByIdOrNull(id: Long): University? {
        return universityRepository.findById(id).orElse(null)
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
        val unis = Gson().fromJsonFile<List<ExportedUniversity>>("source_unis.json")
        val saveUnist = unis.map { it.toDomain() }
        universityRepository.saveAll(saveUnist)
    }


    fun List<String>.toAliases() : String {
        return joinToString(";")
    }

    override fun createTempUniversity(fullName: String): University {
        val uni = University(fullName = fullName, country = "Not Specified", validated = false, countryCode = "ERR")
        return universityRepository.save(uni)
    }
}
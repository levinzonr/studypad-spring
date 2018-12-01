package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.errors.NotFoundException
import com.levinzonr.ezpad.domain.model.University
import com.levinzonr.ezpad.domain.model.User
import com.levinzonr.ezpad.domain.repositories.UniversityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UniversityServiceImpl : UniversityService {

    @Autowired
    private lateinit var universityRepository: UniversityRepository

    override fun findUniversities(query: String): List<University> {
        val all = universityRepository.findAll()
        if (query.isEmpty()) return all.toList()

        return listOf<List<University>>(
                all.filter { it.fullName.startsWith(query, true) }.toList(),
                all.filter { it.shortName.startsWith(query, true) }.toList(),
                all.filter { it.aliases().any {it.startsWith(query, true)} }
        ).flatten().distinctBy { it.id }

    }

    override fun createUniversity(fullName: String, shortName: String, aliases: List<String>?) : University {
        val university = University(fullName = fullName, shortName = shortName, aliases = aliases?.toAliases() ?: "")
        return universityRepository.save(university)
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

        // CVUT
        val cvut = University(
                fullName = "Czech Technical University in Prague",
                shortName = "ČVUT",
                aliases = listOf("České vysoké učení technické v Praze", "ČVUT", "CVUT", "Ceske vysoke uceni technicke").toAliases()
        )

        val vse = University(
                fullName = "University of Economics, Prague",
                shortName = "VŠE",
                aliases = listOf("Vysoká škola ekonomická v Praze", "VSE", "Vysoka skola ekonomicka v praze").toAliases()
        )


        val czu = University(
                fullName = "Czech University of Life Sciences Prague",
                shortName = "ČZU",
                aliases = listOf("Česká zemědělská univerzita v Praze", "CZU", "Ceska zemedelska univerzita").toAliases()
        )

        val a = universityRepository.save(cvut)
        println("Saved cvut: $a")
        universityRepository.save(vse)
        universityRepository.save(czu)
    }


    fun List<String>.toAliases() : String {
        return joinToString(";")
    }

}
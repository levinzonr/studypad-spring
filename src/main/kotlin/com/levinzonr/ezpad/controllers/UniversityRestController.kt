package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.responses.UniversityResponse
import com.levinzonr.ezpad.services.UniversityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/university")
class UniversityRestController {
    @CrossOrigin(origins = ["http://localhost:3000"])
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping("/find")
    fun test() {
        println("tsts")
    }

    @Autowired
    private lateinit var universityService: UniversityService

    @CrossOrigin(origins = ["http://localhost:3000"])
    @GetMapping("/find")
    fun findUniversities(@RequestParam("query") param: String) : List<UniversityResponse> {
        return universityService.findUniversities(param).map { it.toResponse() }
    }

    @GetMapping
    fun findAll() : List<UniversityResponse> {
        return universityService.findAll().map { it.toResponse() }
    }

}
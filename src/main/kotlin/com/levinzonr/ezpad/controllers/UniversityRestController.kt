package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.responses.UniversityResponse
import com.levinzonr.ezpad.services.UniversityService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/university")
class UniversityRestController {

    @Autowired
    private lateinit var universityService: UniversityService

    @GetMapping("/find")
    fun findUniversities(@RequestParam("query") param: String) : List<UniversityResponse> {
        return universityService.findUniversities(param).map { it.toResponse() }
    }

    @GetMapping
    fun findAll() : List<UniversityResponse> {
        return universityService.findAll().map { it.toResponse() }
    }

}
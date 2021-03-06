package com.levinzonr.ezpad.controllers

import com.levinzonr.ezpad.domain.errors.InvalidPayloadException
import com.levinzonr.ezpad.domain.payload.CreateUniversityPayload
import com.levinzonr.ezpad.domain.responses.ErrorResponse
import com.levinzonr.ezpad.domain.responses.UniversityResponse
import com.levinzonr.ezpad.services.UniversityService
import com.sun.xml.internal.ws.client.sei.ResponseBuilder
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/university")
class UniversityRestController {


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


    @DeleteMapping("/{id}")
    fun deleteUniversity(@PathVariable("id") id: Long) {
        universityService.deleteUniversity(id)
    }

    @PatchMapping("/{id}")
    fun updateUniversity(@PathVariable("id") id: Long, @RequestBody createUniversityPayload: CreateUniversityPayload) : UniversityResponse {
        return universityService.updateUniversity(id, createUniversityPayload.fullName, createUniversityPayload.shortName).toResponse()
    }



    @PostMapping("/tmp")
    fun createTemporaryUniversity(@RequestParam(name = "name") name: String) : UniversityResponse {
        return universityService.createTempUniversity(name).toResponse()
    }
}
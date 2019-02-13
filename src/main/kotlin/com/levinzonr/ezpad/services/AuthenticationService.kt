package com.levinzonr.ezpad.services

interface AuthenticationService {

    /**
     * Verifies firebase token and returns user id
     */
    fun userIdFromToken(token: String) : String

    /**
     *
     */
    fun createCustomToken(uuid: String) : String
}
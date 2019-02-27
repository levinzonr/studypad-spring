package com.levinzonr.ezpad.services

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FirebaseAuthService : AuthenticationService {

    @Autowired
    private lateinit var auth: FirebaseAuth

    override fun userIdFromToken(token: String): String {
        throw FirebaseAuthException("code", "mesg")
        return auth.verifyIdToken(token).uid
    }

    override fun createCustomToken(uuid: String): String {
        return auth.createCustomToken(uuid)
    }
}
package com.levinzonr.ezpad.services

import com.google.firebase.auth.FirebaseAuth
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FirebaseAuthService : AuthenticationService {

    @Autowired
    private lateinit var auth: FirebaseAuth

    override fun userIdFromToken(token: String): String {
        return auth.verifyIdToken(token).uid
    }

    override fun createCustomToken(uuid: String): String {
        return auth.createCustomToken(uuid)
    }
}
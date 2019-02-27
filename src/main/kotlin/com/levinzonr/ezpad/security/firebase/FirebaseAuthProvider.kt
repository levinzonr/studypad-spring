package com.levinzonr.ezpad.security.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import org.springframework.security.web.authentication.session.SessionAuthenticationException
import java.util.concurrent.ExecutionException
import com.google.firebase.auth.FirebaseToken
import com.levinzonr.ezpad.domain.errors.AuthException
import com.levinzonr.ezpad.security.StudyPadUserDetails
import com.levinzonr.ezpad.services.AuthenticationService
import com.levinzonr.ezpad.services.FirebaseAuthService
import org.hibernate.boot.model.process.spi.MetadataBuildingProcess.complete
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.AuthenticationException
import java.util.concurrent.CompletableFuture
import java.util.logging.Logger


@Component
class FirebaseAuthProvider : AbstractUserDetailsAuthenticationProvider() {

    @Autowired
    private lateinit var auth: FirebaseAuth


    override fun supports(authentication: Class<*>): Boolean {
        println("${FirebaseAuthToken::class.java.isAssignableFrom(authentication)}")
        return true
    }

    @Throws(AuthenticationException::class)
    override fun additionalAuthenticationChecks(userDetails: UserDetails, authentication: UsernamePasswordAuthenticationToken) {
    }

    @Throws(AuthenticationException::class)
    override fun retrieveUser(username: String, authentication: UsernamePasswordAuthenticationToken): UserDetails {

        val authenticationToken = authentication as FirebaseAuthToken

        print("retrieve user: $username, ${authentication.token}")
            val firebaseToken = auth.verifyIdToken(authenticationToken.token)
            return StudyPadUserDetails(firebaseToken.uid, firebaseToken.email)

    }
}

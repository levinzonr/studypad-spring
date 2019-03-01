package com.levinzonr.ezpad.security.firebase

import com.google.firebase.auth.FirebaseAuthException
import com.levinzonr.ezpad.domain.errors.AuthException
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.lang.RuntimeException
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class FirebaseAuthTokenFilter : AbstractAuthenticationProcessingFilter("/api/**") {


    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val authToken = request?.getHeader(TOKEN_HEADER)
        if (authToken.isNullOrEmpty()) throw RuntimeException("Invalid token")
        print("attemprtAuth: $authToken")
        return try {
            authenticationManager.authenticate(FirebaseAuthToken(authToken))
        } catch (exception: Exception) {
            throw AuthException("${exception.message}")
        }
    }

    override fun unsuccessfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, failed: AuthenticationException?) {
        super.unsuccessfulAuthentication(request, response, failed)
        print("FAILDED")
    }

    override fun successfulAuthentication(request: HttpServletRequest?, response: HttpServletResponse?, chain: FilterChain?, authResult: Authentication?) {
        super.successfulAuthentication(request, response, chain, authResult)
        println("Success auth")
        chain?.doFilter(request, response)
    }


    companion object {
        private const val TOKEN_HEADER = "Firebase"
    }
}
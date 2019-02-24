package com.levinzonr.ezpad.security.firebase

import org.springframework.security.core.Authentication
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
        return authenticationManager.authenticate(FirebaseAuthToken(authToken))
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
package com.levinzonr.ezpad.security

import com.levinzonr.ezpad.security.firebase.FirebaseAuthProvider
import com.levinzonr.ezpad.security.firebase.FirebaseAuthToken
import com.levinzonr.ezpad.security.firebase.FirebaseAuthTokenFilter
import com.levinzonr.ezpad.services.EzpadUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.authentication.ProviderManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.AuthenticationFailureHandler


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {


    @Autowired
    private lateinit var authProvider: FirebaseAuthProvider

    @Autowired
    private lateinit var userDetails: EzpadUserDetailService

    @Autowired
    private lateinit var authEntryPoint: AuthEntryPoint

    @Autowired
    private lateinit var authHandler: AuthenticationFailureHandler

    fun provideFirebaseAuthTokenFilter(): FirebaseAuthTokenFilter {
        val filter = FirebaseAuthTokenFilter()
        filter.setAuthenticationManager(authenticationManager())
        filter.setAuthenticationSuccessHandler { _, _, _ -> }
        return filter
    }

    @Bean
    @Throws(Exception::class)
    public override fun authenticationManager(): AuthenticationManager {
        return ProviderManager(listOf(authProvider))
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.userDetailsService(userDetails)
    }
    override fun configure(http: HttpSecurity?) {
        http?.let { httpSecurity ->

            httpSecurity
                    .cors()
                    .and()
                    .exceptionHandling().authenticationEntryPoint(authEntryPoint).and()
                    .formLogin().failureHandler(authHandler).and()
                    // we don't need CSRF because our token is invulnerable
                    .csrf().disable()
                    // All urls must be authenticated (filter for token always fires (/**)
                    .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers( "/api/**").authenticated()
                    .and()

                    // don't create session
                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) //.and()
            // Custom JWT based security filter
            httpSecurity
                    .addFilterBefore(provideFirebaseAuthTokenFilter(), UsernamePasswordAuthenticationFilter::class.java)


        }
    }
}
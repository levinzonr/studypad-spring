package com.levinzonr.ezpad.security

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class WebSecurityConfiguration : WebSecurityConfigurerAdapter() {


    @Bean
    fun provideAuthenticationManager() : AuthenticationManager {
        return authenticationManager()
    }


    override fun configure(http: HttpSecurity?) {
        http?.authorizeRequests()
                ?.antMatchers(HttpMethod.POST, "/oauth/**")?.permitAll()
                ?.antMatchers(HttpMethod.OPTIONS, "/api/**")?.permitAll()
                ?.antMatchers(HttpMethod.GET, "/api/shared/**")?.permitAll()
                ?.and()
                ?.antMatcher("/api/**")?.authorizeRequests()
                ?.antMatchers(HttpMethod.POST, "/api/users")?.permitAll()
                ?.antMatchers(HttpMethod.GET, "/api/university/find")?.permitAll()
                ?.antMatchers(HttpMethod.GET, "/api/university")?.permitAll()
                ?.anyRequest()?.authenticated()
    }
}
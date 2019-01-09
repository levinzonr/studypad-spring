package com.levinzonr.ezpad.security.resource

import com.levinzonr.ezpad.security.SimpleCORSFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity

import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer

@Configuration
@EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
class ResourceServerConfiguration : ResourceServerConfigurerAdapter() {

    @Autowired
    private lateinit var filter: SimpleCORSFilter

    override fun configure(resources: ResourceServerSecurityConfigurer?) {
        resources?.resourceId(ResourceServerSettings.RESOURCE_ID)
    }


    override fun configure(http: HttpSecurity?) {
        http?.cors()?.disable()?.authorizeRequests()
                ?.antMatchers(HttpMethod.POST, "/oauth/**")?.permitAll()
                ?.antMatchers(HttpMethod.OPTIONS, "/oauth/**")?.permitAll()
                ?.antMatchers("/auth/**")?.permitAll()
                ?.antMatchers(HttpMethod.OPTIONS, "/api/**")?.permitAll()
                ?.antMatchers(HttpMethod.GET, "/api/shared/**")?.permitAll()
                ?.and()
                ?.antMatcher("/api/**")?.authorizeRequests()
                ?.antMatchers(HttpMethod.POST, "/api/users")?.permitAll()
                ?.antMatchers(HttpMethod.GET, "/api/university/find")?.permitAll()
                ?.antMatchers(HttpMethod.GET, "/api/university/**")?.permitAll()
                ?.anyRequest()?.authenticated()
    }

}
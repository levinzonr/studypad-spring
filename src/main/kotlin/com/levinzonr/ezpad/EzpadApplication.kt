package com.levinzonr.ezpad

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore

@SpringBootApplication
class EzpadApplication {


    @Bean
    fun provideTokenStore() : TokenStore {
        return InMemoryTokenStore()
    }

    @Bean
    fun providePasswordEncoder() : BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }


    //Using generated security password: d571634d-2999-4b80-a80d-022b0600e511
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            SpringApplication.run(EzpadApplication::class.java, *args)
        }
    }

}

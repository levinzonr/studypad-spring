package com.levinzonr.ezpad.security.auth

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore
import org.springframework.security.oauth2.provider.token.TokenStore
import java.io.IOException
import org.springframework.util.FileCopyUtils
import org.springframework.core.io.ClassPathResource





@Configuration
class JwtConfiguration {

    @Autowired
    private lateinit var accessTokenConverter: JwtAccessTokenConverter


    @Bean
    @Qualifier("tokenStore")
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter)
    }

    @Bean
    protected fun jwtTokenEnhancer(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        val resource = ClassPathResource("public.cert")
        var publicKey: String? = null
        try {
            publicKey = String(FileCopyUtils.copyToByteArray(resource.inputStream))
        } catch (e: IOException) {
            throw RuntimeException(e)
        }

        converter.setVerifierKey(publicKey)
        return converter
    }

}
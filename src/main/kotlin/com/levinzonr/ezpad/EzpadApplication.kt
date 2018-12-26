package com.levinzonr.ezpad

import com.levinzonr.ezpad.services.UniversityService
import com.levinzonr.ezpad.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore
import org.springframework.boot.CommandLineRunner
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore
import javax.sql.DataSource


@SpringBootApplication
class EzpadApplication {

    @Autowired
    private lateinit var dataSource: DataSource

    @Bean
    fun provideTokenStore() : TokenStore {
        val insertAccessTokenSql = "insert into oauth_access_token (token_id, token, authentication_id, email, client_id, authentication, refresh_token) values (?, ?, ?, ?, ?, ?, ?)"
        val selectAccessTokensFromUserNameAndClientIdSql = "select token_id, token from oauth_access_token where email = ? and client_id = ?"
        val selectAccessTokensFromUserNameSql = "select token_id, token from oauth_access_token where email = ?"
        val selectAccessTokensFromClientIdSql = "select token_id, token from oauth_access_token where client_id = ?"
        val insertRefreshTokenSql = "insert into oauth_refresh_token (token_id, token, authentication) values (?, ?, ?)"

        val jdbcTokenStore = JdbcTokenStore(dataSource)
        jdbcTokenStore.setInsertAccessTokenSql(insertAccessTokenSql)
        jdbcTokenStore.setSelectAccessTokensFromUserNameAndClientIdSql(selectAccessTokensFromUserNameAndClientIdSql)
        jdbcTokenStore.setSelectAccessTokensFromUserNameSql(selectAccessTokensFromUserNameSql)
        jdbcTokenStore.setSelectAccessTokensFromClientIdSql(selectAccessTokensFromClientIdSql)
        jdbcTokenStore.setInsertRefreshTokenSql(insertRefreshTokenSql)


        return jdbcTokenStore
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


    //InMemory database int
    @Bean
    fun initDatabase(universityService: UniversityService, userService: UserService): CommandLineRunner {
       return CommandLineRunner {
           universityService.init()
     //      userService.createUser("roma@mail.ru", "19961600")
       }
    }

}

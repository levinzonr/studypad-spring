package com.levinzonr.ezpad.security.auth

import com.levinzonr.ezpad.security.resource.ResourceServerSettings
import com.levinzonr.ezpad.services.EzpadUserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.TokenStore
import javax.sql.DataSource


@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfiguration : AuthorizationServerConfigurerAdapter() {


    // TODO Fix autowire to inject interface instead of implementation
    @Autowired
    private lateinit var userDetailsService: EzpadUserDetailService

    @Autowired
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var tokenStore: TokenStore

    @Autowired
    private lateinit var authenticationManager: AuthenticationManager

    @Autowired
    private lateinit var dataSource: DataSource

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer?) {
        endpoints?.tokenStore(tokenStore)
                ?.authenticationManager(authenticationManager)
                ?.userDetailsService(userDetailsService)
    }

    override fun configure(clients: ClientDetailsServiceConfigurer?) {
        clients?.jdbc(dataSource)
                ?.withClient(AuthorizationSettings.AUTH_CLIENT_NAME)
                ?.authorizedGrantTypes("password", "refresh_token")
                ?.resourceIds(ResourceServerSettings.RESOURCE_ID)
                ?.secret(passwordEncoder.encode(AuthorizationSettings.AUTH_CLIENT_SECRET))
                ?.scopes("mobile_app")
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer?) {
        security?.passwordEncoder(passwordEncoder)
    }



}

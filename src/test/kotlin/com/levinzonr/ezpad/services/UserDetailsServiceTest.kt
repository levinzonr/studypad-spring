package com.levinzonr.ezpad.services

import com.levinzonr.ezpad.domain.repositories.UserRepository
import org.junit.Test
import org.mockito.Mockito.mock

class UserDetailsServiceTest {


    @Test
    fun testEmailExistence() {
        val userRepository = mock(UserRepository::class.java)
        val service = EzpadUserDetailService().apply {  this.userRepository = userRepository }
    }
    
}
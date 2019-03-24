package com.levinzonr.ezpad

import com.levinzonr.ezpad.services.UniversityService
import com.levinzonr.ezpad.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.boot.CommandLineRunner
import org.springframework.web.bind.annotation.CrossOrigin
import javax.sql.DataSource
import com.google.firebase.FirebaseApp
import org.springframework.core.io.ClassPathResource
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import com.levinzonr.ezpad.security.AuthEntryPoint
import com.levinzonr.ezpad.security.AuthHandler
import com.levinzonr.ezpad.services.TagService
import com.levinzonr.ezpad.services.TopicService
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import java.io.FileInputStream




@SpringBootApplication
@CrossOrigin(origins = ["http://localhost:3000"])
class EzpadApplication {

    @Autowired
    private lateinit var dataSource: DataSource


    @Bean
    fun providePasswordEncoder() : BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    fun provideFirebaseAuthentication() : FirebaseAuth {
        return FirebaseAuth.getInstance()
    }

    @Bean
    fun provideAuthEntryPoint() : AuthenticationEntryPoint{
        return AuthEntryPoint()
    }

    @Bean
    fun provideHandler() : AuthenticationFailureHandler {
        return AuthHandler()
    }

    //Using generated security password: d571634d-2999-4b80-a80d-022b0600e511
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            val serviceAccount = FileInputStream("src/main/resources/firebase-adminsdk.json")

            val options = FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .build()

            FirebaseApp.initializeApp(options)
            runApplication<EzpadApplication>(*args)
        }
    }


    //InMemory database int
    @Bean
    fun initDatabase(universityService: UniversityService, userService: UserService, topicService: TopicService, tagService: TagService): CommandLineRunner {
       return CommandLineRunner {
           universityService.init()
           topicService.init()
           tagService.init()
       }
    }

}

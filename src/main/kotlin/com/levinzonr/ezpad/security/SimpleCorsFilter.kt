package com.levinzonr.ezpad.security

import java.io.IOException
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SimpleCORSFilter : Filter {

    private val log = LoggerFactory.getLogger(SimpleCORSFilter::class.java)

    init {
        log.info("SimpleCORSFilter init")
    }

    @Throws(IOException::class, ServletException::class)
    override fun doFilter(req: ServletRequest, res: ServletResponse, chain: FilterChain) {

        val request = req as HttpServletRequest
        val response = res as HttpServletResponse
        println("Request header: "  + request.getHeader("Origin"))
        response.addHeader("Access-Control-Allow-Origin", "*")
        response.addHeader("Access-Control-Allow-Credentials", "true")
        response.addHeader("Access-Control-Allow-Methods", "OPTIONS, GET, POST, DELETE, PUT, PATCH, HEAD");
        response.addHeader("Access-Control-Max-Age", "3600")
        response.addHeader("Access-Control-Allow-Headers", "authorization, Content-Type, Accept, X-Requested-With, remember-me, access-control-allow-origin, access-control-allow-headers, access-control-allow-methods")

        chain.doFilter(req, res)
    }

    override fun init(filterConfig: FilterConfig) {}

    override fun destroy() {}

}
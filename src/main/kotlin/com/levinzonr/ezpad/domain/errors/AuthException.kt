package com.levinzonr.ezpad.domain.errors

import org.springframework.security.core.AuthenticationException


class AuthException(message: String, e: Throwable? = null) : AuthenticationException(message, e) {
}
package com.levinzonr.ezpad.security.firebase

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

class FirebaseAuthToken(val token: String) : UsernamePasswordAuthenticationToken(null, null) {

}
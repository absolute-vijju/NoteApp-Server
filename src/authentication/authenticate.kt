package com.developer.vijay.authentication

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

private val hashKey = System.getenv("HASH_SECRET_KEY")
private val hmacKey = SecretKeySpec(hashKey.toByteArray(), "HmacSHA1")

fun hash(password: String): String {
    val hmac = Mac.getInstance("HmacSHA1").apply {
        init(hmacKey)
    }
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}
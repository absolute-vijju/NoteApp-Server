package com.developer.vijay.data.model

import io.ktor.auth.*

data class User(
    val email: String,
    val username: String,
    val hashPassword: String
) : Principal

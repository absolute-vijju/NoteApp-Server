package com.developer.vijay.data

import com.developer.vijay.data.model.User

interface UserDAO {

    suspend fun insertUser(user: User): User?

    suspend fun findUserByEmail(email: String): User?

}
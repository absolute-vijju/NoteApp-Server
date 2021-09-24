package com.developer.vijay.repository

import com.developer.vijay.data.model.NoteTable
import com.developer.vijay.data.table.UserTable
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.net.URI

object DatabaseFactory {

    fun init() {
        Database.connect(provideHikari())
        transaction {
            SchemaUtils.create(UserTable)
            SchemaUtils.create(NoteTable)
        }
    }

   /* private fun provideHikari(): HikariDataSource {
        val config = HikariConfig()
        config.setDriverClassName("JDBC_DRIVER")
//        config.jdbcUrl =
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"

        val uri = URI(System.getenv("DATABASE_URL"))
//        val uri = URI("postgres://hkfsurzaqvtbpu:895206e65f1d8862ba39bafb2b8e981d15ea63273f8c17e75a1838e9c3a05730@ec2-18-209-143-227.compute-1.amazonaws.com:5432/d6c2b22603k03r")
        val username = uri.userInfo.split(":").toTypedArray()[0]
        val password = uri.userInfo.split(":").toTypedArray()[1]

        config.jdbcUrl =
            "jdbc:postgresql://" + uri.host + ":" + uri.port + uri.path + "?sslmode=require" + "&user=$username&password=$password"

        config.validate()

        return HikariDataSource(config)
    }*/

    private fun provideHikari(): HikariDataSource {
        val config = HikariConfig()
        config.setDriverClassName(System.getenv("JDBC_DRIVER"))
        config.jdbcUrl = System.getenv("DATABASE_URL")
        config.maximumPoolSize = 3
        config.isAutoCommit = false
        config.transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        config.validate()
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: () -> T): T =
        withContext(Dispatchers.IO) {
            transaction { block() }
        }
}
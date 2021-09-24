package com.developer.vijay.data.model

import com.developer.vijay.data.table.UserTable
import org.jetbrains.exposed.sql.Table

object NoteTable : Table() {

    val id = varchar("id", 512)
    val userEmail = varchar("user_email", 512).references(UserTable.email)
    val title = text("title")   // To store limitless chars
    val description = text("description")
    val date = long("date")

    override val primaryKey: PrimaryKey = PrimaryKey(id)
}
package com.developer.vijay.repository

import com.developer.vijay.data.model.Note
import com.developer.vijay.data.model.NoteTable
import com.developer.vijay.data.model.User
import com.developer.vijay.data.table.UserTable
import com.developer.vijay.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*

class UserRepository {
    suspend fun addUser(user: User) {
        dbQuery {
            UserTable.insert { tblUser ->
                tblUser[email] = user.email
                tblUser[username] = user.username
                tblUser[hashPassword] = user.hashPassword
            }
        }
    }

    suspend fun findUserByEmail(email: String) = dbQuery {
        UserTable.select { UserTable.email.eq(email) }
            .map {
                rowToUser(it)
            }
            .singleOrNull()
    }

    private fun rowToUser(row: ResultRow?): User? {
        if (row == null)
            return null

        return User(
            row[UserTable.email],
            row[UserTable.username],
            row[UserTable.hashPassword]
        )
    }


    // For Notes
    suspend fun insertNote(note: Note, email: String) = dbQuery {
        NoteTable.insert { tblNote ->
            tblNote[NoteTable.id] = note.id
            tblNote[NoteTable.userEmail] = email
            tblNote[NoteTable.title] = note.title
            tblNote[NoteTable.description] = note.description
            tblNote[NoteTable.date] = note.date
        }
    }

    /*
    suspend fun insertNote(note:Note,email: String){
        dbQuery {

            NoteTable.insert { nt->
                nt[NoteTable.id] = note.id
                nt[NoteTable.userEmail] = email
                nt[NoteTable.noteTitle] = note.noteTitle
                nt[NoteTable.description] = note.description
                nt[NoteTable.date] = note.date
            }

        }
    }*/

    suspend fun getAllNotes(email: String): List<Note> = dbQuery {
        NoteTable.select { NoteTable.userEmail.eq(email) }
            .mapNotNull {
                rowToNote(it)
            }
    }

    suspend fun updateNote(note: Note, email: String) = dbQuery {
        NoteTable.update(where = {
            NoteTable.userEmail.eq(email) and NoteTable.id.eq(note.id)
        }) { tblNote ->
            tblNote[NoteTable.title] = note.title
            tblNote[NoteTable.description] = note.description
            tblNote[NoteTable.date] = note.date
        }
    }

    suspend fun deleteNote(id: String, email: String) = dbQuery {
        NoteTable.deleteWhere { NoteTable.id.eq(id) and NoteTable.userEmail.eq(email) }
    }

    private fun rowToNote(row: ResultRow?): Note? {
        if (row == null)
            return null

        return Note(
            row[NoteTable.id],
            row[NoteTable.title],
            row[NoteTable.description],
            row[NoteTable.date]
        )
    }
}
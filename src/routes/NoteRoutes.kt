package com.developer.vijay.routes

import com.developer.vijay.data.model.Note
import com.developer.vijay.data.model.SimpleResponse
import com.developer.vijay.data.model.User
import com.developer.vijay.repository.UserRepository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*


const val NOTES = "$API_VERSION/notes"
const val CREATE_NOTES = "$NOTES/create"
const val UPDATE_NOTES = "$NOTES/update"
const val DELETE_NOTES = "$NOTES/delete"

@Location(CREATE_NOTES)
class CreateNoteRoute

@Location(NOTES)
class NotesRoute

@Location(UPDATE_NOTES)
class UpdateNoteRoute

@Location(DELETE_NOTES)
class DeleteNoteRoute


@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.NoteRoutes(
    db: UserRepository,
    hashFunction: (String) -> String
) {

    authenticate("jwt") {

        post<CreateNoteRoute> {
            val note = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }

            try {

                val email = call.principal<User>()!!.email
                db.insertNote(note, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Added Successfully!"))

            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
            }
        }

        get<NotesRoute> {

            try {
                val email = call.principal<User>()!!.email
                val notes = db.getAllNotes(email)
                call.respond(HttpStatusCode.OK, notes)
            } catch (e: Exception) {

                call.respond(HttpStatusCode.Conflict, emptyList<Note>())
            }

        }

        post<UpdateNoteRoute> {

            val note = try {
                call.receive<Note>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing Fields"))
                return@post
            }

            try {

                val email = call.principal<User>()!!.email
                db.updateNote(note, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Updated Successfully!"))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some Problem Occurred!"))
            }


        }

        delete<DeleteNoteRoute> {

            val noteId = try {
                call.request.queryParameters["id"]!!
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "QueryParameter:id is not present"))
                return@delete
            }


            try {

                val email = call.principal<User>()!!.email
                db.deleteNote(noteId, email)
                call.respond(HttpStatusCode.OK, SimpleResponse(true, "Note Deleted Successfully!"))

            } catch (e: Exception) {
                call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some problem Occurred!"))
            }

        }

    }

}















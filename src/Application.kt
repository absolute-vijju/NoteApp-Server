package com.developer.vijay

import com.developer.vijay.authentication.JwtService
import com.developer.vijay.authentication.hash
import com.developer.vijay.data.model.User
import com.developer.vijay.repository.DatabaseFactory
import com.developer.vijay.repository.UserRepository
import com.developer.vijay.routes.NoteRoutes
import com.developer.vijay.routes.UserRoutes
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.locations.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    DatabaseFactory.init()
    val db = UserRepository()
    val jwtService = JwtService()
    val hashFunction = { s: String -> hash(s) }

    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    install(Authentication) {

        jwt("jwt") {

            verifier(jwtService.verifier)
            realm = "Note Server"
            validate {
                val payload = it.payload
                val email = payload.getClaim("email").asString()
                val user = db.findUserByEmail(email)
                user
            }

        }

    }

    install(ContentNegotiation) {
        gson {

        }
    }

    install(Locations) {

    }

    routing {

        get("/") {
            call.respond("Hello World!")
        }

        UserRoutes(db, jwtService, hashFunction)
        NoteRoutes(db, hashFunction)

        get("/token") {
            val email = call.request.queryParameters["email"]
            val username = call.request.queryParameters["username"]
            val password = call.request.queryParameters["password"]

            call.respond(jwtService.getAuthToken(User(email!!, username!!, password!!)))
        }
    }
}

fun practiceRoutes(route: Route) {
    route {
        // Params http://0.0.0.0:8080/note/86
        get("/note/{id}") {
            val id = call.parameters["id"]
            call.respond("You passed $id in Params")
        }

        // Query Params http://0.0.0.0:8080/note?id=86
        get("/note") {
            val id = call.request.queryParameters["id"]
            call.respond("You passed $id in Query Params")
        }

        route("/notes") {
            get {
                call.respond("This is GET request")
            }

            post {
                val body = call.receive<String>()
                call.respond("This is POST request $body")
            }

            delete {
                val body = call.receive<String>()
                call.respond("This is DELETE request $body")
            }
        }
    }
}

data class MySession(val count: Int = 0)


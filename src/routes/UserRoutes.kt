package com.developer.vijay.routes

import com.developer.vijay.authentication.JwtService
import com.developer.vijay.authentication.hash
import com.developer.vijay.data.model.LoginRequest
import com.developer.vijay.data.model.RegisterRequest
import com.developer.vijay.data.model.SimpleResponse
import com.developer.vijay.data.model.User
import com.developer.vijay.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val API_VERSION = "/v1"
const val USERS = "$API_VERSION/users"
const val REGISTER_REQUEST = "$USERS/register"
const val LOGIN_REQUEST = "$USERS/login"

@Location(REGISTER_REQUEST)
class UserRegisterRoute

@Location(LOGIN_REQUEST)
class UserLoginRoute

@OptIn(KtorExperimentalLocationsAPI::class)
fun Route.UserRoutes(
    db: UserRepository,
    jwtService: JwtService,
    hashFunction: (String) -> String
) {
    post<UserRegisterRoute> {
        val registerRequest = try {
            call.receive<RegisterRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing some params."))
            return@post
        }

        try {
            val user = User(registerRequest.email, registerRequest.username, hashFunction(registerRequest.password))
            db.addUser(user)
            call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.getAuthToken(user)))
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some problem occurred."))
        }
    }

    post<UserLoginRoute> {
        val loginRequest = try {
            call.receive<LoginRequest>()
        } catch (e: Exception) {
            call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Missing some params."))
            return@post
        }

        try {
            val user = db.findUserByEmail(loginRequest.email)
            if (user == null)
                call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong email."))
            else {
                if (user.hashPassword == hashFunction(loginRequest.password))
                    call.respond(HttpStatusCode.OK, SimpleResponse(true, jwtService.getAuthToken(user)))
                else
                    call.respond(HttpStatusCode.BadRequest, SimpleResponse(false, "Wrong password."))
            }
        } catch (e: Exception) {
            call.respond(HttpStatusCode.Conflict, SimpleResponse(false, e.message ?: "Some problem occurred."))
        }
    }
}




















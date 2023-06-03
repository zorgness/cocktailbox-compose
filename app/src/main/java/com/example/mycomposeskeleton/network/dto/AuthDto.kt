package com.example.mycomposeskeleton.network.dto

import android.os.Parcelable
import com.squareup.moshi.Json

data class LoginDto(
    @Json(name= "email") val email: String,
    @Json(name= "password") val password: String
)

data class RegisterDto(
    @Json(name= "email") val email: String,
    @Json(name= "password") val password: String,
    @Json(name= "username") val username: String,
)

data class SessionDto(
    @Json(name = "token")
    val token: String,
    @Json(name = "status")
    val status: Int,
    @Json(name = "id")
    val id: Int,
)

data class UpdateDto(
    @Json(name= "email") val email: String,
    @Json(name="username") val username: String,
)


data class UserDto(
    @Json(name = "id")
    val id: Int,
    @Json(name = "email")
    val email: String,
    @Json(name = "username")
    val username: String,
)
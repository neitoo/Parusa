package com.example.parusa.Interface

import com.example.parusa.Model.User
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("users/all")
    suspend fun getUsers(@HeaderMap headers: Map<String, String>): List<User>

    @DELETE("users/{id}")
    suspend fun deleteUser(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Response<Unit>
}
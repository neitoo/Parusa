package com.example.parusa.Interface

import com.example.parusa.Model.User
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.POST
import retrofit2.http.Url

interface UserService {
    @GET("users/all")
    suspend fun getUsers(@HeaderMap headers: Map<String, String>): List<User>
}
package com.example.parusa.Interface

import com.example.parusa.Model.User
import com.example.parusa.Model.UserReg
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface UserApi {
    @POST("users/")
    fun createUser(@Header("Authorization") token: String, @Body userData: UserReg): Call<UserReg>
}
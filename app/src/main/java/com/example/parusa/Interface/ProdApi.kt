package com.example.parusa.Interface

import com.example.parusa.Model.Prod
import com.example.parusa.Model.UserReg
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ProdApi {
    @POST("products/")
    fun createProd(@Header("Authorization") token: String, @Body prodData: Prod): Call<Prod>
}
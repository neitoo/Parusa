package com.example.parusa.Interface

import com.example.parusa.Model.Prod
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface ProductService {
    @GET("products/all")
    suspend fun getProduct(@HeaderMap headers: Map<String, String>): List<Prod>
}
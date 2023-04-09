package com.example.parusa.Interface

import com.example.parusa.Model.Prod
import com.example.parusa.Model.ProdData
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HeaderMap
import retrofit2.http.Path

interface ProductService {
    @GET("products/all")
    suspend fun getProduct(@HeaderMap headers: Map<String, String>): List<ProdData>

    @DELETE("products/{id}")
    suspend fun deleteUser(
        @HeaderMap headers: Map<String, String>,
        @Path("id") id: Int
    ): Response<Unit>
}
package com.example.parusa.Interface

import com.example.parusa.Model.Category
import retrofit2.http.GET
import retrofit2.http.HeaderMap

interface CategoryService {
    @GET("categories/all")
    suspend fun getCategories(@HeaderMap headers: Map<String, String>): List<Category>
}
package com.example.parusa.Model

data class UserReg(
    val name: String,
    val surname: String,
    val email: String,
    val password: String,
    val role: Int,
    val is_admin: Int
)

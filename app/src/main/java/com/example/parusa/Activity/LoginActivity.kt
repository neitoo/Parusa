package com.example.parusa.Activity

import android.content.Context
import android.content.Intent
import android.media.session.MediaSession
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.example.parusa.databinding.LoginActivityBinding
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okio.IOException
import org.json.JSONObject


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: LoginActivityBinding
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = LoginActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.loginBtn.setOnClickListener {
            when{
                TextUtils.isEmpty(binding.emailPlain.text.toString().trim{ it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Пожалуйста введите email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                TextUtils.isEmpty(binding.passwordPlain.text.toString().trim{ it <= ' '}) -> {
                    Toast.makeText(
                        this@LoginActivity,
                        "Пожалуйста введите пароль.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                else ->{
                    val email: String = binding.emailPlain.text.toString().trim{ it <= ' '}
                    val password: String =binding.passwordPlain.text.toString().trim {it <= ' '}

                    validateAndNavigate(email,password)
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()

        // Check if tokens are already saved in SharedPreferences
        val sharedPrefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val accessToken = sharedPrefs.getString("access_token", null)
        val refreshToken = sharedPrefs.getString("refresh_token", null)

        if (accessToken != null && refreshToken != null) {
            // Navigate to the main activity
            val intent = Intent(this@LoginActivity, AdminActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun isValidEmail(email: String): Boolean {
        return email.contains("@")
    }

    fun isValidPassword(password: String): Boolean {
        return password.length >= 8
    }

    fun validateAndNavigate(email: String, password: String) {
        if (isValidEmail(email) && isValidPassword(password)) {
            login(email, password)

        } else {
            Toast.makeText(this,"Проверьте поле емайл, или длину пароля > 8", Toast.LENGTH_SHORT).show()
        }
    }

    private fun login(email: String, password: String) {
        val jsonObject = JSONObject()
        jsonObject.put("email", email)
        jsonObject.put("password", password)

        val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonObject.toString())

        val request = Request.Builder()
            .url("http://82.148.18.70:5001/auth/login")
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("LoginActivity", "Failed to execute request: ${e.message}")
            }

            override fun onResponse(call: Call, response: Response) {val responseBody = response.body?.string()
                Log.d("LoginActivity", "Response body: $responseBody")

                if (response.isSuccessful && responseBody != null) {
                    val responseJson = JSONObject(responseBody)
                    val accessToken = responseJson.getString("access_token")
                    val refreshToken = responseJson.getString("refresh_token")

                    // Save tokens to SharedPreferences
                    val sharedPrefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
                    val editor = sharedPrefs.edit()
                    editor.putString("access_token", accessToken)
                    editor.putString("refresh_token", refreshToken)
                    editor.apply()

                    // Navigate to the main activity
                    val intent = Intent(this@LoginActivity, AdminActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        })
    }

}
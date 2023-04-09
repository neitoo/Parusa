package com.example.parusa.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.parusa.Interface.ProdApi
import com.example.parusa.Interface.UserApi
import com.example.parusa.Model.Prod
import com.example.parusa.Model.UserReg
import com.example.parusa.R
import com.example.parusa.databinding.ActivityAddNewProductBinding
import com.example.parusa.databinding.ActivityAddNewUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddNewProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewProductBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backAdminBtn.setOnClickListener{
            onBackPressed()
        }

        binding.addNewUserBtn.setOnClickListener {
            if (validateFields()) {
                val category = binding.addCategoryPlain.text.toString().toInt()
                val title = binding.addTitlePlain.text.toString()

                val newProd = Prod(category, title)

                addProduct(newProd)
            } else {
                Toast.makeText(this, "Не все поля заполнены верно", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        val category = binding.addCategoryPlain.text.toString()
        val title = binding.addTitlePlain.text.toString()

        if (category.isEmpty() || title.isEmpty()) {
            return false
        }

        return true
    }

    private fun addProduct(pr: Prod) {
        // Получение токена из SharedPreferences
        val sharedPrefs = getSharedPreferences("auth", MODE_PRIVATE)
        val token = sharedPrefs.getString("access_token", "") ?: ""

        // Создание объекта Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://82.148.18.70:5001/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Создание объекта UserApi
        val prodApi = retrofit.create(ProdApi::class.java)

        // Создание объекта Call для выполнения POST запроса на сервер
        val call = prodApi.createProd("Bearer $token", pr)

        // Асинхронный вызов метода createUser() через объект Call
        call.enqueue(object : Callback<Prod> {
            override fun onResponse(call: Call<Prod>, response: Response<Prod>) {
                val intent = Intent(this@AddNewProductActivity, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<Prod>, t: Throwable) {
                // Обработка ошибки при выполнении запроса на сервер
            }
        })
    }
}
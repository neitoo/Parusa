package com.example.parusa.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.example.parusa.Interface.UserApi
import com.example.parusa.Model.UserReg
import com.example.parusa.R
import com.example.parusa.databinding.ActivityAddNewUserBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AddNewUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewUserBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var spinnerAdapter = ArrayAdapter.createFromResource(
            this,
            R.array.roles,
            R.layout.color_spinner_layout
        )

        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item_layout)
        binding.roleSpinner.adapter = spinnerAdapter

        binding.backAdminBtn.setOnClickListener{
            onBackPressed()
        }

        binding.addNewUserBtn.setOnClickListener {
            if (validateFields()) {
                val name = binding.addNamePlain.text.toString()
                val surname = binding.addSurnamePlain.text.toString()
                val email = binding.addEmailPlain.text.toString()
                val password = binding.addPasswordPlain.text.toString()
                val role = binding.roleSpinner.selectedItemPosition
                val isAdmin = if (binding.adminCheckBox.isChecked) 1 else 0

                val user = UserReg(name, surname, email, password, role, isAdmin)

                register(user)
            } else {
                Toast.makeText(this, "Не все поля заполнены верно", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateFields(): Boolean {
        val name = binding.addNamePlain.text.toString()
        val surname = binding.addSurnamePlain.text.toString()
        val email = binding.addEmailPlain.text.toString()
        val password = binding.addPasswordPlain.text.toString()
        val role = binding.roleSpinner.selectedItemPosition

        if (name.isEmpty() || surname.isEmpty()) {
            return false
        }

        if (!email.contains("@")) {
            return false
        }

        if (password.length < 8) {
            return false
        }

        if (role == 0) {
            return false
        }

        return true
    }

    private fun register(user: UserReg) {
        // Получение токена из SharedPreferences
        val sharedPrefs = getSharedPreferences("auth", MODE_PRIVATE)
        val token = sharedPrefs.getString("access_token", "") ?: ""

        // Создание объекта Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("http://82.148.18.70/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Создание объекта UserApi
        val userApi = retrofit.create(UserApi::class.java)

        // Создание объекта Call для выполнения POST запроса на сервер
        val call = userApi.createUser("Bearer $token", user)

        // Асинхронный вызов метода createUser() через объект Call
        call.enqueue(object : Callback<UserReg> {
            override fun onResponse(call: Call<UserReg>, response: Response<UserReg>) {
                val intent = Intent(this@AddNewUserActivity, AdminActivity::class.java)
                startActivity(intent)
                finish()
            }

            override fun onFailure(call: Call<UserReg>, t: Throwable) {
                // Обработка ошибки при выполнении запроса на сервер
            }
        })
    }
}
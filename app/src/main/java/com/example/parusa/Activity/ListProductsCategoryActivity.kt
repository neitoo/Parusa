package com.example.parusa.Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parusa.Interface.CategoryService
import com.example.parusa.Interface.UserService
import com.example.parusa.Model.CategoryAdapter
import com.example.parusa.Model.UserAdapter
import com.example.parusa.R
import com.example.parusa.databinding.ActivityAdminBinding
import com.example.parusa.databinding.ActivityListProductsCategoryBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ListProductsCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityListProductsCategoryBinding

    private lateinit var adapter: CategoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListProductsCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backAdminBtn.setOnClickListener {
            onBackPressed()
        }

        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPreferences?.getString("access_token", "") ?: ""

        val retrofit = Retrofit.Builder()
            .baseUrl("http://82.148.18.70:5001/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val categService = retrofit.create(CategoryService::class.java)

        val recyclerView = findViewById<RecyclerView>(R.id.categRecView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = CategoryAdapter(emptyList())
        recyclerView.adapter = adapter

        GlobalScope.launch {
            try {
                val headers = mapOf("Authorization" to "Bearer $token")
                Log.d("headers", "$headers")
                val categorys = categService.getCategories(headers)
                withContext(Dispatchers.Main) {
                    adapter.updateData(categorys)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: HttpException) {
                Log.e("Users", "Failed to get users", e)
            } catch (e: Throwable) {
                Log.e("Users", "Failed to get users", e)
            }
        }
    }
}
package com.example.parusa.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parusa.Interface.ProductService
import com.example.parusa.Interface.UserService
import com.example.parusa.Model.Prod
import com.example.parusa.Model.ProductAdapter
import com.example.parusa.Model.UserAdapter
import com.example.parusa.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class Product : Fragment() {
    private val sharedPreferences by lazy {
        context?.getSharedPreferences("auth", Context.MODE_PRIVATE)
    }

    private val token by lazy {
        sharedPreferences?.getString("access_token", "") ?: ""
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://82.148.18.70/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val prodService = retrofit.create(ProductService::class.java)

    private lateinit var adapter: ProductAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_product, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.productRecView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = ProductAdapter(emptyList())
        recyclerView.adapter = adapter

        GlobalScope.launch {
            try {
                val headers = mapOf("Authorization" to "Bearer $token")
                val products = prodService.getProduct(headers)
                withContext(Dispatchers.Main) {
                    adapter.updateData(products)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: HttpException) {
                Log.e("Product", "Failed to get product", e)
            } catch (e: Throwable) {
                Log.e("Product", "Failed to get product", e)
            }
        }

        return view
    }


}
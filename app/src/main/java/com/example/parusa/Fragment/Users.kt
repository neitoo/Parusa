package com.example.parusa.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parusa.Interface.UserService
import com.example.parusa.Model.UserAdapter
import com.example.parusa.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Headers
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Users : Fragment() {

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

    private val userService = retrofit.create(UserService::class.java)

    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_users, container, false)

        val recyclerView = view.findViewById<RecyclerView>(R.id.userRecView)
        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = UserAdapter(emptyList())
        recyclerView.adapter = adapter

        GlobalScope.launch {
            try {
                val headers = mapOf("Authorization" to "Bearer $token")
                Log.d("headers", "$headers")
                val users = userService.getUsers(headers)
                withContext(Dispatchers.Main) {
                    adapter.updateData(users)
                    adapter.notifyDataSetChanged()
                }
            } catch (e: HttpException) {
                Log.e("Users", "Failed to get users", e)
            } catch (e: Throwable) {
                Log.e("Users", "Failed to get users", e)
            }
        }

        return view
    }
}
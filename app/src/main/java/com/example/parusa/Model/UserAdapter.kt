package com.example.parusa.Model

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.parusa.Interface.UserService
import com.example.parusa.R
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UserAdapter(private var userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emailText: TextView = view.findViewById(R.id.emailText)
        val nameText: TextView = view.findViewById(R.id.nameText)
        val surnameText: TextView = view.findViewById(R.id.surnameText)
        val roleText: TextView = view.findViewById(R.id.roleText)
        val deleteBtn: ImageButton = view.findViewById(R.id.deleteUserBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_user_layout, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.emailText.text = "Емайл: " + (user.email ?: "отсутствует")
        holder.nameText.text = "Имя: " + (user.name ?: "отсутствует")
        holder.surnameText.text = "Фамилия: " + (user.surname ?: "отсутствует")
        when (user.role) {
            "0" -> holder.roleText.text = "Роль: отсутствует"
            "1" -> holder.roleText.text = "Роль: без роли"
            "2" -> holder.roleText.text = "Роль: управляющий"
            "3" -> holder.roleText.text = "Роль: аналитик"
            else -> holder.roleText.text = "Роль: закупщик"
        }
        holder.deleteBtn.setOnClickListener {
            val alertLogout = AlertDialog.Builder(it.context)
            alertLogout.setTitle("Удалить пользователя?")
            alertLogout.setIcon(R.mipmap.ic_launcher)
            alertLogout.setPositiveButton("Да"){ dialogInterface: DialogInterface, id: Int ->
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://82.148.18.70:5001/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val userService = retrofit.create(UserService::class.java)

                val sharedPrefs = it.context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("access_token", "") ?: ""

                val headers = mapOf("Authorization" to "Bearer $token")

                val userId = user.id

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = userService.deleteUser(headers, userId)
                        if (response.isSuccessful) {
                            // удаление прошло успешно, обновление данных
                            val newData = userService.getUsers(headers)
                            withContext(Dispatchers.Main) {
                                updateData(newData)
                            }
                        } else {
                            // обработка ошибки
                        }
                    } catch (e: Exception) {
                        // обработка ошибки
                    }
                }
            }
            alertLogout.setNegativeButton("Нет"){dialogInterface: DialogInterface, id: Int ->
                dialogInterface.dismiss()

            }
            alertLogout.show()


        }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateData(newData:List<User>) {
        userList = newData
        notifyDataSetChanged()
    }
}
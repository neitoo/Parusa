package com.example.parusa.Model

import android.content.Context
import android.content.DialogInterface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.parusa.Interface.UserService
import com.example.parusa.R
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class UserAdapter(private var userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emailText: TextView = view.findViewById(R.id.emailText)
        val nameText: TextView = view.findViewById(R.id.nameText)
        val surnameText: TextView = view.findViewById(R.id.surnameText)
        val roleText: TextView = view.findViewById(R.id.roleText)
        val deleteBtn: ImageButton = view.findViewById(R.id.deleteUserBtn)

        fun showEditDialog() {
            val user = userList[adapterPosition]


            val inflater = LayoutInflater.from(itemView.context)
            val viewInflated = inflater.inflate(R.layout.layout_dialog_change, null)

            val alertDialog = AlertDialog.Builder(itemView.context)
                .setView(viewInflated)
                .create()

            val emailInput = viewInflated.findViewById<EditText>(R.id.updateEmailPlain)
            val nameInput = viewInflated.findViewById<EditText>(R.id.updateNamePlain)
            val surnameInput = viewInflated.findViewById<EditText>(R.id.updateSurnamePlain)
            val roleSpinner = viewInflated.findViewById<Spinner>(R.id.updateRoleSpinner)


            val saveButton = viewInflated.findViewById<Button>(R.id.saveChangeBtn)
            val cancelButton = viewInflated.findViewById<Button>(R.id.cancelBtn)

            emailInput.setText(user.email)
            nameInput.setText(user.name)
            surnameInput.setText(user.surname)

            val roles = itemView.resources.getStringArray(R.array.roles)
            val roleAdapter = ArrayAdapter(itemView.context, android.R.layout.simple_spinner_dropdown_item, roles)
            roleSpinner.adapter = roleAdapter
            roleSpinner.setSelection(user.role!!.toInt())

            alertDialog.apply {
                setView(viewInflated)

                saveButton.setOnClickListener {

                    user.email = emailInput.text.toString()
                    user.name = nameInput.text.toString()
                    user.surname = surnameInput.text.toString()
                    user.role = roleSpinner.selectedItemPosition.toString()

                    val retrofit = Retrofit.Builder()
                        .baseUrl("http://82.148.18.70/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val userService = retrofit.create(UserService::class.java)

                    val sharedPrefs = itemView.context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                    val token = sharedPrefs.getString("access_token", "") ?: ""

                    val headers = mapOf("Authorization" to "Bearer $token")

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val response = userService.updateUser(headers, user.id, user)
                            if (response.isSuccessful) {
                                val newData = userService.getUsers(headers)
                                withContext(Dispatchers.Main) {
                                    updateData(newData)
                                }
                            } else {
                                Log.e("UserService", "Failed to update user. Response code: ${response.code()}")
                            }
                        } catch (e: Exception) {
                            Log.e("Error228", e.message, e)
                        }
                    }
                    alertDialog.dismiss()
                }

                cancelButton.setOnClickListener {
                    alertDialog.dismiss()
                }

                show()
            }
        }
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
                    .baseUrl("http://82.148.18.70/")
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

        holder.itemView.setOnClickListener {
            holder.showEditDialog()
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
package com.example.parusa.Model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parusa.R

class UserAdapter(private var userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val emailText: TextView = view.findViewById(R.id.emailText)
        val nameText: TextView = view.findViewById(R.id.nameText)
        val surnameText: TextView = view.findViewById(R.id.surnameText)
        val roleText: TextView = view.findViewById(R.id.roleText)
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
        holder.roleText.text = "Роль: " + (user.role ?: "отсутствует")
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    fun updateData(newData:List<User>) {
        userList = newData
        notifyDataSetChanged()
    }
}
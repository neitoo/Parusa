package com.example.parusa.Model

import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.parusa.Interface.UserService
import com.example.parusa.R

class CategoryAdapter (private var categList: List<Category>) :
    RecyclerView.Adapter<CategoryAdapter.CategViewHolder>() {

    inner class CategViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val id: TextView = view.findViewById(R.id.idCategText)
        val title: TextView = view.findViewById(R.id.nameCategText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_category_layout, parent, false)
        return CategViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CategViewHolder, position: Int) {
        val categ = categList[position]
        holder.id.text = "Категория: " + categ.id
        holder.title.text = "Название: " + categ.title

    }

    override fun getItemCount(): Int {
        return categList.size
    }

    fun updateData(newData:List<Category>) {
        categList = newData
        notifyDataSetChanged()
    }
}
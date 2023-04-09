package com.example.parusa.Model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.parusa.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductAdapter(private var prodList: List<Prod>) :
RecyclerView.Adapter<ProductAdapter.ProdViewHolder>() {

    inner class ProdViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val idText: TextView = view.findViewById(R.id.idText)
        val nameText: TextView = view.findViewById(R.id.nameProductText)
        val deleteBtn: ImageButton = view.findViewById(R.id.deleteProductBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProdViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_product_layout, parent, false)
        return ProdViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProdViewHolder, position: Int) {
        val prod = prodList[position]
        holder.idText.text = "Категория: " + prod.category
        holder.nameText.text = "Название: " + prod.title

        holder.deleteBtn.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return prodList.size
    }

    fun updateData(newData:List<Prod>) {
        prodList = newData
        notifyDataSetChanged()
    }
}
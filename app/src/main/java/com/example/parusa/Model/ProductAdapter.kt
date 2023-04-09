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
import com.example.parusa.Interface.ProductService
import com.example.parusa.Interface.UserService
import com.example.parusa.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ProductAdapter(private var prodList: List<ProdData>) :
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
            val alertLogout = AlertDialog.Builder(it.context)
            alertLogout.setTitle("Удалить продукт?")
            alertLogout.setIcon(R.mipmap.ic_launcher)
            alertLogout.setPositiveButton("Да"){ dialogInterface: DialogInterface, id: Int ->
                val retrofit = Retrofit.Builder()
                    .baseUrl("http://82.148.18.70:5001/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                val prodService = retrofit.create(ProductService::class.java)

                val sharedPrefs = it.context.getSharedPreferences("auth", Context.MODE_PRIVATE)
                val token = sharedPrefs.getString("access_token", "") ?: ""

                val headers = mapOf("Authorization" to "Bearer $token")

                val prodId = prod.id

                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val response = prodService.deleteUser(headers, prodId)
                        if (response.isSuccessful) {
                            // удаление прошло успешно, обновление данных
                            val newData = prodService.getProduct(headers)
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
            alertLogout.setNegativeButton("Нет"){ dialogInterface: DialogInterface, id: Int ->
                dialogInterface.dismiss()

            }
            alertLogout.show()


        }
    }

    override fun getItemCount(): Int {
        return prodList.size
    }

    fun updateData(newData:List<ProdData>) {
        prodList = newData
        notifyDataSetChanged()
    }
}
package com.example.parusa.Activity

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.example.parusa.Fragment.Coffee
import com.example.parusa.Fragment.Product
import com.example.parusa.Fragment.Users
import com.example.parusa.R
import com.example.parusa.databinding.ActivityAdminBinding

class AdminActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        replaceFragment(Users())

        binding.logoutBtn.setOnClickListener {

            val alertLogout = AlertDialog.Builder(this)
            alertLogout.setTitle("Выйти из аккаунта?")
            alertLogout.setIcon(R.mipmap.ic_launcher)
            alertLogout.setPositiveButton("Да"){ dialogInterface: DialogInterface, id: Int ->
                val sharedPrefs = getSharedPreferences("auth", Context.MODE_PRIVATE)
                val editor = sharedPrefs.edit()
                editor.remove("access_token")
                editor.remove("refresh_token")
                editor.apply()

                // Navigate back to login activity
                val intent = Intent(this@AdminActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            alertLogout.setNegativeButton("Нет"){dialogInterface: DialogInterface, id: Int ->
                dialogInterface.dismiss()

            }
            alertLogout.show()

        }

        binding.addUserBtn.setOnClickListener {
            val intent = Intent(this@AdminActivity, AddNewUserActivity::class.java)
            startActivity(intent)
        }
        binding.addProductBtn.setOnClickListener {
            val intent = Intent(this@AdminActivity, AddNewProductActivity::class.java)
            startActivity(intent)
        }
        binding.addCoffeesBtn.setOnClickListener {

        }

        binding.infoCateg.setOnClickListener {
            val intent = Intent(this@AdminActivity, ListProductsCategoryActivity::class.java)
            startActivity(intent)
        }


        binding.navigationBottom.setOnItemSelectedListener {
            binding.addUserBtn.visibility = View.GONE
            binding.addProductBtn.visibility = View.GONE
            binding.addCoffeesBtn.visibility = View.GONE
            when(it.itemId){
                R.id.users -> {
                    replaceFragment(Users())
                    binding.addUserBtn.visibility = View.VISIBLE
                    binding.infoCateg.visibility = View.GONE
                }
                R.id.products -> {
                    replaceFragment(Product())
                    binding.addProductBtn.visibility = View.VISIBLE
                    binding.infoCateg.visibility = View.VISIBLE
                }
                R.id.coffe -> {
                    replaceFragment(Coffee())
                    binding.addCoffeesBtn.visibility = View.VISIBLE
                    binding.infoCateg.visibility = View.GONE
                }
                else -> {}
            }
            true
        }
    }

    private  fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
}
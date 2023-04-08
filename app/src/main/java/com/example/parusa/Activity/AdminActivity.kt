package com.example.parusa.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.parusa.Fragment.Coffee
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

        binding.navigationBottom.setOnItemSelectedListener {
            when(it.itemId){
                R.id.users -> replaceFragment(Users())
                R.id.coffe -> replaceFragment(Coffee())
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
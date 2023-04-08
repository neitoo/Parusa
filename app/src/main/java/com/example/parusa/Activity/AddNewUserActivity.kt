package com.example.parusa.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.parusa.R
import com.example.parusa.databinding.ActivityAddNewUserBinding

class AddNewUserActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddNewUserBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewUserBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
package com.joseph.unsplash_mvvm.ui.user_detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joseph.unsplash_mvvm.R
import com.joseph.unsplash_mvvm.databinding.ActivityUserDetailBinding

class UserDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityUserDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
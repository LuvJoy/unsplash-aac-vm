package com.joseph.unsplash_mvvm.ui.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.joseph.unsplash_mvvm.databinding.ActivityDetailBinding
import timber.log.Timber

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent != null) {
            val id = intent.getStringExtra("id")
            Timber.d("[id] : " + id)
        }
    }
}
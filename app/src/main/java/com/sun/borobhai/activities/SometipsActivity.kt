package com.sun.borobhai.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.borobhai.R
import com.sun.borobhai.databinding.ActivitySometipsBinding


class SometipsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySometipsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySometipsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.headerTextView.text = "Some tips"
        val tips = getString(R.string.some_tips)
        binding.tipsTextView.text = tips
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
package com.sun.borobhai.activities

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.bumptech.glide.Glide
import com.sun.borobhai.MainActivity
import com.sun.borobhai.R
import com.sun.borobhai.databinding.ActivitySplashScreenBinding
import com.sun.borobhai.helper.Helper

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {
    private lateinit var binding : ActivitySplashScreenBinding
    private lateinit var topAnim : Animation
    private lateinit var bottomAnim : Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(Helper().FLAG_LAYOUT_NO_LIMITS, Helper().FLAG_LAYOUT_NO_LIMITS)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Glide.with(this).load(R.drawable.loading).into(binding.imageView2)
        topAnim = AnimationUtils.loadAnimation(this, R.anim.top_to_bottom)
        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_to_top)
        binding.imageView.startAnimation(topAnim)
        binding.textView1.startAnimation(bottomAnim)
        binding.textView2.startAnimation(bottomAnim)
        binding.imageView2.startAnimation(bottomAnim)
        delayTimer()
    }
    private fun delayTimer() {
        Handler(Looper.getMainLooper()).postDelayed({
            goToNext()
        },3500)
    }
    private fun goToNext() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
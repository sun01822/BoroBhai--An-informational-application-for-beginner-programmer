package com.sun.borobhai.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.sun.borobhai.MainActivity
import com.sun.borobhai.databinding.ActivityLogInBinding
import com.sun.borobhai.helper.Helper

class LogIn : AppCompatActivity() {
    private lateinit var binding : ActivityLogInBinding
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(Helper().FLAG_LAYOUT_NO_LIMITS, Helper().FLAG_LAYOUT_NO_LIMITS)
        binding = ActivityLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()

        binding.signup.setOnClickListener {
            startActivity(Intent(this, SignUp::class.java))
            finish()
        }
        binding.login.setOnClickListener {
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Check if the user's email is verified
                            val user = auth.currentUser
                            if (user?.isEmailVerified == true) {
                                // Email is verified, proceed to the main activity
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()
                            } else {
                                // Email is not verified, show a message and prevent login
                                Toast.makeText(
                                    this,
                                    "Please verify your email before logging in",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            val errorMessage = task.exception?.message
                            Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
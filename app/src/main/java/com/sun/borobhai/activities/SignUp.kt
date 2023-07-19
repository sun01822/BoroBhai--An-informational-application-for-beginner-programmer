package com.sun.borobhai.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.sun.borobhai.R
import com.sun.borobhai.databinding.ActivitySignUpBinding

class SignUp : AppCompatActivity() {
    private lateinit var binding : ActivitySignUpBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var imageUrl : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()


        binding.login.setOnClickListener{
            startActivity(Intent(this, LogIn::class.java))
            finish()
        }

        binding.signup.setOnClickListener {
            val name = binding.name.text.toString().trim()
            val batch = binding.batch.text.toString().trim()
            val semester = binding.semester.text.toString().trim()
            val email = binding.email.text.toString().trim()
            val password = binding.password.text.toString().trim()
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/borobhai-1c547.appspot.com/o/profile.png?alt=media&token=df8bad33-6a34-4ab3-805e-aa7f72c2ae61"

            if (name.isNotEmpty() && batch.isNotEmpty() && semester.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val uid = user?.uid
                            if (uid != null) {
                                // Save user information to Realtime Database
                                val userRef = database.reference.child("users").child(uid)
                                val userData = HashMap<String, Any>()
                                userData["name"] = name
                                userData["batch"] = batch
                                userData["semester"] = semester
                                userData["email"] = email
                                userData["imageUrl"] = imageUrl

                                userRef.setValue(userData)
                                    .addOnCompleteListener { dbTask ->
                                        if (dbTask.isSuccessful) {
                                            // Send email verification
                                            sendEmailVerification()
                                        } else {
                                            Toast.makeText(
                                                this,
                                                "Failed to save user data",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
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
    private fun sendEmailVerification() {
        val user = auth.currentUser
        user?.sendEmailVerification()
            ?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Verification email sent", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this, LogIn::class.java))
                    finish()
                } else {
                    val errorMessage = task.exception?.message
                    Toast.makeText(this, "Error: $errorMessage", Toast.LENGTH_SHORT).show()
                }
            }
    }

}
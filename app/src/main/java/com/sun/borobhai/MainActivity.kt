package com.sun.borobhai

import android.content.Intent
import android.graphics.drawable.ClipDrawable.HORIZONTAL
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sun.borobhai.activities.LogIn
import com.sun.borobhai.databinding.ActivityMainBinding
import com.sun.borobhai.databinding.NavHeaderMainBinding
import de.hdodenhof.circleimageview.CircleImageView

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileImage: CircleImageView

    private val imageList = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val headerView = navigationView.getHeaderView(0)
        val navHeaderMainBinding = NavHeaderMainBinding.bind(headerView)
        profileName = navHeaderMainBinding.profileName
        profileEmail = navHeaderMainBinding.profileEmail
        profileImage = navHeaderMainBinding.profileImage

        val uid = auth.currentUser?.uid
        val userRef = database.reference.child("users").child(uid!!)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val name = snapshot.child("name").value.toString()
                val email = snapshot.child("email").value.toString()
                val imageUrl = snapshot.child("imageUrl").value.toString()

                profileName.text = name
                profileEmail.text = email

                Glide.with(this@MainActivity)
                    .load(imageUrl)
                    .circleCrop()
                    .placeholder(R.drawable.logo)
                    .into(profileImage)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this@MainActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })



        if(auth.currentUser == null){
            startActivity(Intent(this, LogIn::class.java))
            finish()
        }
    }
}
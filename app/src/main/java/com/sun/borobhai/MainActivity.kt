package com.sun.borobhai

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.sun.borobhai.activities.LogIn
import com.sun.borobhai.adapter.ImageSliderAdapter
import com.sun.borobhai.databinding.ActivityMainBinding
import com.sun.borobhai.databinding.NavHeaderMainBinding
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private lateinit var auth : FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileImage: CircleImageView
    private lateinit var imageRecyclerView: RecyclerView
    private lateinit var imageSliderAdapter: ImageSliderAdapter
    private lateinit var timer: Timer

    private val imageList = mutableListOf(
        "https://i.pinimg.com/originals/e7/1d/00/e71d00d119507842a4b850678446fc46.jpg",
        "https://wallpaperaccess.com/full/8992598.jpg",
        "https://e1.pxfuel.com/desktop-wallpaper/699/446/desktop-wallpaper-black-programming-programming-quotes.jpg",
        "https://i.pinimg.com/originals/c9/88/9c/c9889cfcdb4204f02255db89c78e14a7.jpg"
    )



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navigationView = binding.navView

        imageRecyclerView = binding.imageSliderRecyclerView
        imageRecyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imageSliderAdapter = ImageSliderAdapter(this, imageList)
        imageRecyclerView.adapter = imageSliderAdapter

        timer = Timer()
        timer.scheduleAtFixedRate(SliderTimer(imageRecyclerView), 3000, 3000)


        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val headerView = navigationView.getHeaderView(0)
        val navHeaderMainBinding = NavHeaderMainBinding.bind(headerView)
        profileName = navHeaderMainBinding.profileName
        profileEmail = navHeaderMainBinding.profileEmail
        profileImage = navHeaderMainBinding.profileImage

        val uid = auth.currentUser?.uid
        if (auth.currentUser == null) {
            startActivity(Intent(this, LogIn::class.java))
            finish()
        } else {
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

            binding.drawerButton.setOnClickListener{
                if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    drawerLayout.openDrawer(GravityCompat.START)
                } else {
                    drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

            navigationView.setNavigationItemSelectedListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.nav_logout -> {
                        // Handle logout menu item click
                        logout()
                        true
                    }
                    // Handle other menu item clicks if needed
                    else -> false
                }
            }
        }

    }
    private fun logout() {
        auth.signOut()
        Toast.makeText(this, "Logout successfully!!!", Toast.LENGTH_LONG).show()
        // Redirect to the login screen after logout
        val intent = Intent(this, LogIn::class.java)
        startActivity(intent)
        finish() // Optional: Close the MainActivity after logout
    }

    // Handle the navigation drawer icon click to open/close the drawer
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                drawerLayout.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}


package com.sun.borobhai

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.sun.borobhai.fragment.HomeFragment
import com.sun.borobhai.helper.Helper
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*


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
        "https://cdn.quotesgram.com/img/73/31/1523667431-steve-jobs-coding-quote.jpg",
        "https://www.azquotes.com/picture-quotes/quote-we-re-changing-the-world-with-technology-bill-gates-57-0-032.jpg",
        "https://www.azquotes.com/picture-quotes/quote-the-trouble-with-programmers-is-that-you-can-never-tell-what-a-programmer-is-doing-until-seymour-cray-54-57-60.jpg",
        "https://www.azquotes.com/picture-quotes/quote-basic-is-to-computer-programming-as-qwerty-is-to-typing-seymour-papert-84-82-56.jpg",
        "https://www.azquotes.com/vangogh-image-quotes/72/21/Quotation-Mark-Zuckerberg-My-number-one-piece-of-advice-is-you-should-learn-72-21-21.jpg"

    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(Helper().FLAG_LAYOUT_NO_LIMITS, Helper().FLAG_LAYOUT_NO_LIMITS)
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
        timer.scheduleAtFixedRate(SliderTimer(imageRecyclerView), 4000, 4000)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        val headerView = navigationView.getHeaderView(0)
        val navHeaderMainBinding = NavHeaderMainBinding.bind(headerView)
        profileName = navHeaderMainBinding.profileName
        profileEmail = navHeaderMainBinding.profileEmail
        profileImage = navHeaderMainBinding.profileImage

        val fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()

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
        val toggle = ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.open_nav, R.string.close_nav)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

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


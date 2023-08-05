package com.sun.borobhai.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RatingBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.sun.borobhai.R
import com.sun.borobhai.adapter.RatingsAdapter
import com.sun.borobhai.data.Rating
import com.sun.borobhai.databinding.ActivityRateUsBinding

class RateUsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRateUsBinding
    private lateinit var databaseReference: DatabaseReference
    private lateinit var ratingsAdapter: RatingsAdapter
    private var hasUserRated: Boolean = false // To track if the user has already rated

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRateUsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val initialRatingValue = 0.0F
        // Set the initial rating value on tvRatingValue
        updateRatingValue(initialRatingValue)

        binding.ratingBar.setOnRatingBarChangeListener { _, ratingValue, _ ->
            // Update tvRatingValue when a star is selected
            updateRatingValue(ratingValue)
        }
        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().reference.child("ratings")

        // Set up RecyclerView and adapter
        binding.rvRatings.layoutManager = LinearLayoutManager(this)
        ratingsAdapter = RatingsAdapter()
        binding.rvRatings.adapter = ratingsAdapter

        // Set up the Submit Rating button click listener
        binding.btnSubmit.setOnClickListener {
            submitRating()
        }

        // Check if the user has already rated
        checkUserRating()

        // Fetch and display the ratings from Firebase
        fetchRatingsFromFirebase()

        // Listen for changes in the Firebase database
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the adapter before adding new data
                ratingsAdapter.clear()

                // Iterate through the database snapshot and add ratings to the adapter
                for (ratingSnapshot in snapshot.children) {
                    val rating = ratingSnapshot.getValue(Rating::class.java)
                    rating?.let { ratingsAdapter.addRating(it) }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that might occur during data retrieval
                Toast.makeText(this@RateUsActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun checkUserRating() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        databaseReference.orderByChild("userId").equalTo(userId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                hasUserRated = snapshot.exists()
                // Hide the submit button if the user has already rated
                binding.btnSubmit.isEnabled = !hasUserRated
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that might occur during data retrieval
                Toast.makeText(this@RateUsActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun submitRating() {
        // Get the rating value and comment from the UI components
        val ratingValue = binding.ratingBar.rating
        val comment = binding.etComment.text.toString()

        // Get the current user's ID
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        // Get the current timestamp
        val timestamp = System.currentTimeMillis()

        // Create a new Rating object
        val newRating = Rating(userId, ratingValue, comment, timestamp)

        // Generate a new unique key for the rating entry in Firebase
        val ratingKey = databaseReference.push().key

        // Save the new rating to Firebase
        ratingKey?.let {
            databaseReference.child(it).setValue(newRating)
                .addOnSuccessListener {
                    // Rating saved successfully
                    Toast.makeText(this, "Rating submitted", Toast.LENGTH_SHORT).show()
                    // Clear RatingBar and EditText
                    binding.ratingBar.rating = 0.0F
                    binding.etComment.text.clear()
                    // Update the flag to indicate that the user has rated
                    hasUserRated = true
                    // Hide the submit button after rating
                    binding.btnSubmit.isEnabled = false
                    binding.ratingBar.isEnabled = false
                    binding.etComment.isEnabled = false
                }
                .addOnFailureListener {
                    // Failed to save the rating
                    Toast.makeText(this, "Failed to submit rating", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun updateRatingValue(ratingValue: Float) {
        // Update tvRatingValue to show the selected rating
        binding.tvRatingValue.text = String.format("%.1f", ratingValue)
    }

    private fun fetchRatingsFromFirebase() {
        // Fetch the ratings data from Firebase
        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val ratings: MutableList<Rating> = mutableListOf()
                // Iterate through the database snapshot and add ratings to the list
                for (ratingSnapshot in snapshot.children) {
                    val rating = ratingSnapshot.getValue(Rating::class.java)
                    rating?.let { ratings.add(it) }
                }
                // Update the RatingsAdapter with the new ratings data
                ratingsAdapter.updateRatingsData(ratings)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle any errors that might occur during data retrieval
                Toast.makeText(this@RateUsActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}

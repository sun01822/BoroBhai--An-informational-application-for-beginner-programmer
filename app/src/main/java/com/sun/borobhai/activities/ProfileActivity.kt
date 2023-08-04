package com.sun.borobhai.activities

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.sun.borobhai.R
import com.sun.borobhai.databinding.ActivityProfleBinding

@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {
    private lateinit var binding : ActivityProfleBinding
    private lateinit var auth : FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val loadingColor = ContextCompat.getColor(this, R.color.black)
        binding.progressBar.indeterminateTintList = ColorStateList.valueOf(loadingColor)

        auth = FirebaseAuth.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        databaseReference = FirebaseDatabase.getInstance().reference.child("users").child(userId)

        binding.profileImage.setOnClickListener {
            openImagePicker()
        }

        binding.update.setOnClickListener {
            updateProfileData()
        }

        binding.backButton.setOnClickListener {
            onBackPressed()
        }

        loadUserData()
    }

    private fun loadUserData() {
        // Fetch user data from Firebase Realtime Database
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Retrieve data from the snapshot
                    val name = snapshot.child("name").getValue(String::class.java)
                    val semester = snapshot.child("semester").getValue(String::class.java)
                    val batch = snapshot.child("batch").getValue(String::class.java)
                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)

                    // Display user data on the UI
                    binding.fullName.setText(name)
                    binding.semesterEt.setText(semester)
                    binding.batchEt.setText(batch)
                    if (!imageUrl.isNullOrEmpty()) {
                        Glide.with(this@ProfileActivity).load(imageUrl).into(binding.profileImage)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error if necessary
            }
        })
    }

    private fun updateProfileData() {
        // Disable the submit button and show the progress bar
        binding.update.isEnabled = false
        binding.progressBar.visibility = View.VISIBLE

        // Get updated values from the EditTexts
        val name = binding.fullName.text.toString().trim()
        val semester = binding.semesterEt.text.toString().trim()
        val batch = binding.batchEt.text.toString().trim()

        // Get the selected image URI
        val imageUri = selectedImageUri

        // Upload the image to Firebase Storage
        if (imageUri != null) {
            val imageRef = FirebaseStorage.getInstance().reference.child("profile_images/${auth.currentUser?.uid}")
            imageRef.putFile(imageUri)
                .addOnSuccessListener {
                    // Get the download URL of the uploaded image
                    imageRef.downloadUrl.addOnSuccessListener { uri ->
                        // Update the profile data in Firebase Realtime Database with the image URL
                        val profileData = mapOf(
                            "name" to name,
                            "semester" to semester,
                            "batch" to batch,
                            "imageUrl" to uri.toString()
                        )
                        databaseReference.updateChildren(profileData)
                            .addOnCompleteListener { task ->
                                binding.update.isEnabled = true
                                binding.progressBar.visibility = View.GONE
                                if (task.isSuccessful) {
                                    Toast.makeText(this, "Profile updated successfully.", Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show()
                                }
                            }
                    }
                }
                .addOnFailureListener {
                    // Handle the failure if necessary
                }
        } else {
            // If no new image is selected, update the profile data without the image URL
            val profileData = mapOf(
                "name" to name,
                "semester" to semester,
                "batch" to batch
            )

            databaseReference.updateChildren(profileData)
                .addOnCompleteListener { task ->
                    binding.update.isEnabled = true
                    binding.progressBar.visibility = View.GONE
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Profile updated successfully.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, "Failed to update profile.", Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun openImagePicker() {
        // Create an intent to pick an image from the gallery
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_PICKER)
    }

    // Handle the result of the image picker
    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == Activity.RESULT_OK) {
            // Get the selected image URI from the data
            selectedImageUri = data?.data
            // Display the selected image using Glide or any other image loading library
            if (selectedImageUri != null) {
                Glide.with(this@ProfileActivity).load(selectedImageUri).into(binding.profileImage)
            }
        }
    }

    companion object {
        private const val REQUEST_IMAGE_PICKER = 1
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

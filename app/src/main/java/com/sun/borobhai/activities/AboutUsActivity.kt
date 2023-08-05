package com.sun.borobhai.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.sun.borobhai.R
import com.sun.borobhai.databinding.ActivityAboutUsBinding

class AboutUsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityAboutUsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAboutUsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // Set developer details (Replace with actual data)
        Glide.with(this).load(R.drawable.developer).into(binding.developerImage)
        val developer = getString(R.string.developer)
        binding.nameTextView.text = developer // Replace with the developer's name
        val description = getString(R.string.app_description)
        binding.descriptionTextView.text = description
        // Set click listeners for social media buttons
        binding.facebookButton.setOnClickListener {
            openSocialMediaLink("https://www.facebook.com/sun01822/")
        }

        binding.whatsappButton.setOnClickListener {
            openSocialMediaLink("https://wa.link/udetih")
        }

        binding.linkedInButton.setOnClickListener {
            openSocialMediaLink("https://www.linkedin.com/in/md-shariar-hossain-sun-aa77621ab/")
        }

        binding.gmailButton.setOnClickListener {
            openSocialMediaLink("mdshariarhossainsun01822@gmail.com")
        }
        // Add click listeners for other social media buttons as needed
    }

    private fun openSocialMediaLink(url: String) {
        val intent = if (url.startsWith("http://") || url.startsWith("https://")) {
            Intent(Intent.ACTION_VIEW, Uri.parse(url))
        } else {
            Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$url"))
        }
        startActivity(intent)
    }
}

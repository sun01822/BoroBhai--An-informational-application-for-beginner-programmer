package com.sun.borobhai.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.borobhai.databinding.ActivityShareBinding

class ShareActivity : AppCompatActivity() {
    private lateinit var binding : ActivityShareBinding
    private lateinit var shareContent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityShareBinding.inflate(layoutInflater)
        setContentView(binding.root)

        shareContent = "https://www.example.com" // Replace with the link you want to share

        // Share button
        binding.btnShare.setOnClickListener {
            shareLinkToSocialMedia(shareContent)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    private fun shareLinkToSocialMedia(shareContent: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareContent)

        // Create a chooser to allow the user to pick from available apps
        val chooserIntent = Intent.createChooser(intent, "Share Link via")
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooserIntent)
        } else {
            // No apps on the device can handle the sharing intent.
        }
    }
}

package com.sun.borobhai.activities

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.sun.borobhai.R
import com.sun.borobhai.databinding.ActivityDetailsScreenBinding
import com.sun.borobhai.helper.Helper

class DetailsScreen : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsScreenBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(Helper().FLAG_LAYOUT_NO_LIMITS, Helper().FLAG_LAYOUT_NO_LIMITS)
        binding = ActivityDetailsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val link = intent.getStringExtra(EXTRA_LINK)
        val youtubeLink = intent.getIntExtra("youtubeLink", 0)
        if (!link.isNullOrEmpty()) {
            if(youtubeLink!=0){
                openYoutubeChannel(link)
            }else{
                loadLinkInWebView(link)
            }
        } else {
            // Handle the case where the link is null or empty
            Toast.makeText(this, "Link is empty", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadLinkInWebView(link: String) {
        val webView = findViewById<WebView>(R.id.webView)
        webView.webViewClient = WebViewClient()
        webView.loadUrl(link)
    }

    companion object {
        const val EXTRA_LINK = "extra_link"
    }

    fun openYoutubeChannel(youtubeLink: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.setPackage("com.google.android.youtube")

        try {
            startActivity(intent)
            finish()
        } catch (e: ActivityNotFoundException) {
            // YouTube app is not installed, open in web browser
            intent.setPackage(null)
            startActivity(intent)
        }
    }
}
package com.sun.borobhai.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
        if (!link.isNullOrEmpty()) {
            loadLinkInWebView(link)
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
}
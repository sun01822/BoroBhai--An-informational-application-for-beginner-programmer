package com.sun.borobhai.activities

import android.os.Bundle
import android.view.View
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.sun.borobhai.R
import com.sun.borobhai.databinding.ActivityDetailsScreenBinding
import com.sun.borobhai.helper.Helper
import java.util.regex.Pattern

class DetailsScreen : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsScreenBinding
    private lateinit var youTubePlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.setFlags(Helper().FLAG_LAYOUT_NO_LIMITS, Helper().FLAG_LAYOUT_NO_LIMITS)

        val link = intent.getStringExtra(EXTRA_LINK)
        val youtubeLink = intent.getStringExtra("youtubeLink")

        if (youtubeLink == "y") {
            binding.youtubePlayerView.visibility = View.VISIBLE
            binding.webView.visibility = View.GONE
            val videoId = getYouTubeVideoId(link.toString())
            println("VideoID: $videoId")
            initializeYouTubePlayer(videoId)
        } else {
            binding.youtubePlayerView.visibility = View.GONE
            binding.webView.visibility = View.VISIBLE
            loadLinkInWebView(link.toString())
        }
    }

    private fun loadLinkInWebView(link: String) {
        val webView = binding.webView
        webView.webViewClient = WebViewClient()
        webView.loadUrl(link)
    }

    companion object {
        const val EXTRA_LINK = "extra_link"
    }

    private fun getYouTubeVideoId(youtubeLink: String): String? {
        val regex =
            "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/|youtu.be\\/|watch\\?v=|\\/videos\\/|embed\\/)[^#\\&\\?]*"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(youtubeLink)
        return if (matcher.find()) {
            matcher.group()
        } else {
            null
        }
    }

    private fun initializeYouTubePlayer(videoId: String?) {
        if (!videoId.isNullOrEmpty()) {
            youTubePlayerView = findViewById(R.id.youtube_player_view)
            lifecycle.addObserver(youTubePlayerView)

            youTubePlayerView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                override fun onReady(youTubePlayer: YouTubePlayer) {
                    youTubePlayer.loadVideo(videoId, 0f)
                }
            })
        } else {
            Toast.makeText(this, "Invalid YouTube video link", Toast.LENGTH_SHORT).show()
            finish() // Finish the activity if YouTube video link is invalid
        }
    }
}

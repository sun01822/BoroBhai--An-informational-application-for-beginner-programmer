package com.sun.borobhai

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class SliderTimer(private val recyclerView: RecyclerView) : TimerTask() {
    override fun run() {
        recyclerView.post {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val currentPosition = layoutManager.findFirstVisibleItemPosition()
            val newPosition = if (currentPosition == recyclerView.adapter?.itemCount?.minus(1)) 0 else currentPosition + 1
            recyclerView.smoothScrollToPosition(newPosition)
        }
    }
}
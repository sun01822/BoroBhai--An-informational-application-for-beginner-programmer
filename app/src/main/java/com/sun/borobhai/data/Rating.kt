package com.sun.borobhai.data

data class Rating(
    val userId: String? = null,
    val ratingValue: Float = 0.0f,
    val comment: String = "",
    val timestamp: Long = 0L
)

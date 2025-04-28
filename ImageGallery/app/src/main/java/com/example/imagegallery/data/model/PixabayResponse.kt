package com.example.imagegallery.data.model
data class PixabayResponse(
    val totalHits: Int,
    val hits: List<ImageItem>,
    val total: Int
)
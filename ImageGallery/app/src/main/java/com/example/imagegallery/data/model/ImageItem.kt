package com.example.imagegallery.data.model

data class ImageItem(
    val id: Int,
    val previewURL: String,
    val webformatURL: String,
    val largeImageURL: String,
    val tags: String,
    val user: String
)
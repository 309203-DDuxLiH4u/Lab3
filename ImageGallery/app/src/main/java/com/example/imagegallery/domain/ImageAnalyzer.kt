package com.example.imagegallery.domain

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageAnalyzer @Inject constructor(
    private val context: Context,
    private val imageLoader: ImageLoader
) {
    private val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)

    suspend fun analyzeImage(imageUrl: String): List<String> = withContext(Dispatchers.IO) {
        try {
            val request = ImageRequest.Builder(context)
                .data(imageUrl)
                .allowHardware(false) // Quan trọng cho việc lấy Bitmap
                .build()

            val result = imageLoader.execute(request)

            if (result is SuccessResult) {
                val bitmap = (result.drawable as BitmapDrawable).bitmap

                val image = InputImage.fromBitmap(bitmap, 0)

                val labels = labeler.process(image).await()

                val topLabels = labels.sortedByDescending { it.confidence }
                    .take(3)
                    .map { it.text }
                Log.d("ImageAnalyzer", "Labels for $imageUrl: $topLabels")
                topLabels

            } else {
                Log.e("ImageAnalyzer", "Failed to load image: $imageUrl")
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ImageAnalyzer", "Error analyzing image $imageUrl: ${e.message}", e)
            emptyList()
        }
    }
}

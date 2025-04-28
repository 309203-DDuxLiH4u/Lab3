package com.example.imagegallery.data.network

import com.example.imagegallery.data.model.PixabayResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApiService {

    companion object {
        const val BASE_URL = "https://pixabay.com/"

        const val API_KEY = "49982564-284f3943f8182a3e6f488a323"
    }

    @GET("api/")
    suspend fun searchImages(
        @Query("key") apiKey: String = API_KEY,
        @Query("q") query: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int,
        @Query("image_type") imageType: String = "photo"
    ): PixabayResponse
}
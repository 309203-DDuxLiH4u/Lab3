package com.example.imagegallery.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.imagegallery.data.local.ImageAnalysisDao
import com.example.imagegallery.data.model.ProcessedImage
import com.example.imagegallery.data.network.PixabayApiService
import com.example.imagegallery.data.paging.PixabayPagingSource
import com.example.imagegallery.domain.ImageAnalyzer
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ImageRepository @Inject constructor(
    private val pixabayApiService: PixabayApiService,
    private val imageAnalyzer: ImageAnalyzer,
    private val analysisDao: ImageAnalysisDao,
    private val gson: Gson
) {

    fun getSearchResultStream(query: String): Flow<PagingData<ProcessedImage>> {
        return Pager(
            config = PagingConfig(
                pageSize = PixabayPagingSource.NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PixabayPagingSource(pixabayApiService, query, imageAnalyzer, analysisDao, gson)
            }
        ).flow
    }
}
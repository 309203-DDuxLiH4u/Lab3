package com.example.imagegallery.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.imagegallery.data.local.ImageAnalysisDao
import com.example.imagegallery.data.model.AnalysisResult
import com.example.imagegallery.data.model.ImageItem
import com.example.imagegallery.data.model.ProcessedImage
import com.example.imagegallery.data.network.PixabayApiService
import com.example.imagegallery.domain.ImageAnalyzer
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class PixabayPagingSource(
    private val pixabayApiService: PixabayApiService,
    private val query: String,
    private val imageAnalyzer: ImageAnalyzer,
    private val analysisDao: ImageAnalysisDao,
    private val gson: Gson
) : PagingSource<Int, ProcessedImage>() {

    companion object {
        const val STARTING_PAGE_INDEX = 1
        const val NETWORK_PAGE_SIZE = 20
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProcessedImage> {
        val page = params.key ?: STARTING_PAGE_INDEX
        if (query.isBlank()) {
            return LoadResult.Page(data = emptyList(), prevKey = null, nextKey = null)
        }

        return try {
            val response = pixabayApiService.searchImages(
                query = query,
                page = page,
                perPage = params.loadSize
            )
            val images = response.hits

            val processedImages = images.map { imageItem ->

                var aiTags = analysisDao.getAnalysis(imageItem.id)

                if (aiTags == null) {
                    aiTags = imageAnalyzer.analyzeImage(imageItem.previewURL)

                    val analysisResult = AnalysisResult(
                        imageId = imageItem.id,
                        tagsJson = gson.toJson(aiTags)
                    )
                    analysisDao.insertAnalysis(analysisResult)
                }
                ProcessedImage(imageItem = imageItem, aiTags = aiTags)
            }

            LoadResult.Page(
                data = processedImages,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (images.isEmpty()) null else page + (params.loadSize / NETWORK_PAGE_SIZE) // Tính toán nextKey dựa trên params.loadSize
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }


    override fun getRefreshKey(state: PagingState<Int, ProcessedImage>): Int? {

        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
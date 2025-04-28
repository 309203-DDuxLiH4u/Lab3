package com.example.newspagingapp.paging
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.newspagingapp.data.Article
import com.example.newspagingapp.network.NewsApiService
import retrofit2.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class NewsPagingSource(
    private val newsApiService: NewsApiService,
    private val country: String
) : PagingSource<Int, Article>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Article> {
        val page = params.key ?: STARTING_PAGE_INDEX
        val pageSize = params.loadSize
        return try {
            val response = newsApiService.getTopHeadlines(
                country = country,
                page = page,
                pageSize = pageSize
            )
            if (response.isSuccessful) {
                val newsResponse = response.body()
                val articles = newsResponse?.articles ?: emptyList()
                val prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (articles.isEmpty()) null else page + 1
                LoadResult.Page(
                    data = articles,
                    prevKey = prevKey,
                    nextKey = nextKey
                )
            } else {
                LoadResult.Error(HttpException(response))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
    override fun getRefreshKey(state: PagingState<Int, Article>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}
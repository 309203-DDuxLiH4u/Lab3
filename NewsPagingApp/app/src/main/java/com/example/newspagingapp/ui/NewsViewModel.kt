package com.example.newspagingapp.ui
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.newspagingapp.data.Article
import com.example.newspagingapp.network.RetrofitClient
import com.example.newspagingapp.paging.NewsPagingSource
import kotlinx.coroutines.flow.Flow

class NewsViewModel : ViewModel() {
    private val newsApiService = RetrofitClient.instance
    val newsPager: Flow<PagingData<Article>> = Pager(
        config = PagingConfig(
            pageSize = 20,
            enablePlaceholders = false,
        ),
        pagingSourceFactory = { NewsPagingSource(newsApiService, "us") }
    ).flow
        .cachedIn(viewModelScope)
}
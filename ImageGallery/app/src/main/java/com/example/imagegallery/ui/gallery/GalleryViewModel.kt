package com.example.imagegallery.ui.gallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import com.example.imagegallery.data.ImageRepository
import com.example.imagegallery.data.model.ProcessedImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(
    private val repository: ImageRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()


    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val images: Flow<PagingData<ProcessedImage>> = searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            repository.getSearchResultStream(query)
                .map { pagingData ->
                    if (query.isBlank()) {
                        pagingData
                    } else {
                        pagingData.filter { processedImage ->
                            processedImage.aiTags.any { tag ->
                                tag.contains(query, ignoreCase = true)
                            }
                        }
                    }
                }
        }
        .cachedIn(viewModelScope)
}
package com.example.imagegallery.ui.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import com.example.imagegallery.databinding.ActivityMainBinding // Import ViewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: GalleryViewModel by viewModels()
    private lateinit var imageAdapter: ImageAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        setupSearchView()
        observeImageData()
        observeLoadState()

        binding.buttonRetry.setOnClickListener { imageAdapter.retry() }
    }

    private fun setupRecyclerView() {
        imageAdapter = ImageAdapter()
        binding.recyclerView.apply {
            adapter = imageAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2) // 2 cột
            setHasFixedSize(true)
        }
        binding.recyclerView.adapter = imageAdapter.withLoadStateHeaderAndFooter(
            header = ImageLoadStateAdapter { imageAdapter.retry() },
            footer = ImageLoadStateAdapter { imageAdapter.retry() }
        )
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.setSearchQuery(it)
                    binding.recyclerView.scrollToPosition(0)
                }
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.setSearchQuery(newText.orEmpty())
                return true
            }
        })
    }

    private fun observeImageData() {
        lifecycleScope.launch {
            viewModel.images.collectLatest { pagingData ->
                imageAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadState() {
        lifecycleScope.launch {
            imageAdapter.loadStateFlow.collectLatest { loadStates ->
                binding.progressBar.isVisible = loadStates.refresh is LoadState.Loading

                val refreshError = loadStates.refresh as? LoadState.Error
                binding.textViewError.isVisible = refreshError != null
                binding.buttonRetry.isVisible = refreshError != null
                binding.textViewError.text = refreshError?.error?.localizedMessage ?: "An error occurred"

                binding.recyclerView.isVisible = loadStates.refresh !is LoadState.Loading && loadStates.refresh !is LoadState.Error

                val appendError = loadStates.append as? LoadState.Error
                if (appendError != null) {
                    android.widget.Toast.makeText(this@MainActivity, "Error loading more images", android.widget.Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

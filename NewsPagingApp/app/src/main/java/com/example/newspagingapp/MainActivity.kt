package com.example.newspagingapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.newspagingapp.databinding.ActivityMainBinding
import com.example.newspagingapp.ui.NewsAdapter
import com.example.newspagingapp.ui.NewsViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: NewsViewModel by viewModels() // Khởi tạo ViewModel
    private lateinit var newsAdapter: NewsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        observeNewsData()
        observeLoadState()

        binding.btnRetry.setOnClickListener {
            newsAdapter.retry()
        }
    }

    private fun setupRecyclerView() {
        newsAdapter = NewsAdapter()
        binding.recyclerViewNews.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = newsAdapter
        }
    }

    private fun observeNewsData() {
        lifecycleScope.launch {

            viewModel.newsPager.collectLatest { pagingData ->
                newsAdapter.submitData(pagingData)
            }
        }
    }

    private fun observeLoadState() {
        lifecycleScope.launch {
            newsAdapter.loadStateFlow.collectLatest { loadStates ->

                val refreshState = loadStates.refresh

                binding.progressBar.visibility = if (refreshState is LoadState.Loading) View.VISIBLE else View.GONE

                if (refreshState is LoadState.Error) {
                    binding.tvError.visibility = View.VISIBLE
                    binding.btnRetry.visibility = View.VISIBLE
                } else {
                    binding.tvError.visibility = View.GONE
                    binding.btnRetry.visibility = View.GONE
                }
                val appendState = loadStates.append

            }
        }
    }
}
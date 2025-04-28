package com.example.imagegallery.ui.gallery

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.imagegallery.R
import com.example.imagegallery.data.model.ProcessedImage
import com.example.imagegallery.databinding.ListItemImageBinding
private val IMAGE_COMPARATOR = object : DiffUtil.ItemCallback<ProcessedImage>() {
    override fun areItemsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage) =
        oldItem.imageItem.id == newItem.imageItem.id

    override fun areContentsTheSame(oldItem: ProcessedImage, newItem: ProcessedImage) =
        oldItem == newItem
}
    class ImageAdapter : PagingDataAdapter<ProcessedImage, ImageAdapter.ImageViewHolder>(IMAGE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding =
            ListItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    class ImageViewHolder(private val binding: ListItemImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(processedImage: ProcessedImage) {
            binding.apply {
                imageView.load(processedImage.imageItem.webformatURL) {
                    crossfade(true)
                    placeholder(R.drawable.ic_placeholder)
                    error(R.drawable.ic_error)
                    textViewTags.text = "Tags: ${processedImage.imageItem.tags}"
                    textViewAiTags.text = "AI Tags: ${processedImage.aiTags.joinToString()}"
                }
            }
        }
    }
}
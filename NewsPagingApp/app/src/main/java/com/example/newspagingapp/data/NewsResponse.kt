package com.example.newspagingapp.data
import com.google.gson.annotations.SerializedName
data class NewsResponse(
    @SerializedName("status") val status: String,
    @SerializedName("totalResults") val totalResults: Int,
    @SerializedName("articles") val articles: List<Article>
)
data class Article(
    @SerializedName("source") val source: Source?, // Có thể null
    @SerializedName("author") val author: String?, // Có thể null
    @SerializedName("title") val title: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlToImage") val urlToImage: String?, // Ảnh thumbnail
    @SerializedName("publishedAt") val publishedAt: String?, // Định dạng ISO 8601
    @SerializedName("content") val content: String?
)
data class Source(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?
)

package com.seamless.models
// Main response wrapper
data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<NewsArticle>
)

// News source (nested in article)
data class NewsSource(
    val id: String?,
    val name: String
)

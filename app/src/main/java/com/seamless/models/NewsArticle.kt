package com.seamless.models

data class NewsArticle(
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val url: String?,
    val publishedAt: String?,
    val content: String?,
    val author: String?
)

package com.seamless.models

import com.seamless.roomdatabase.ArticleEntity

data class ApiResponse(
    val status: String,
    val totalResults: Int,
    val articles: List<ArticleDto>
)

data class ArticleDto(
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?
)

data class Source(
    val id: String?,
    val name: String
)

// Mapper function to convert DTO to Entity
fun ArticleDto.toEntity(): ArticleEntity {
    return ArticleEntity(
        //sourceName = this.source.name,
        author = this.author,
        title = this.title,
        description = this.description,
        url = this.url,
        urlToImage = this.urlToImage,
        publishedAt = this.publishedAt,
        content = this.content
    )
}

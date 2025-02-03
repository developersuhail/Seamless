package com.seamless.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.seamless.api.RetrofitClient
import com.seamless.models.NewsArticle
import com.seamless.roomdatabase.AppDatabase
import com.seamless.roomdatabase.ArticleEntity
import kotlinx.coroutines.GlobalScope


import kotlinx.coroutines.launch


class NewsViewModel(application: Application) : AndroidViewModel(application) {


    private val articleDao = AppDatabase.getDatabase(application).articleDao()


    private val _newsArticles = MutableLiveData<List<NewsArticle>>()
    val newsArticles: LiveData<List<NewsArticle>> get() = _newsArticles

    fun fetchNews() {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.newsApiService.getTopHeadlines()
                if (response.isSuccessful && response.body() != null) {
                    val articles = response.body()!!.articles.map {
                        NewsArticle(
                            title = it.title ?: "",
                            description = it.description ?: "",
                            urlToImage = it.urlToImage,
                            content = it.content ?: "",
                            //source = it.source,
                            author = it.author,
                            publishedAt = it.publishedAt,
                            url = it.url
                        )
                    }
                    // Save to Room database
                    // Convert NewsArticle to ArticleEntity
                    val articleEntities = articles.map {
                        ArticleEntity(
                            title = it.title,
                            description = it.description,
                            urlToImage = it.urlToImage,
                            content = it.content,
                            author = it.author,
                            publishedAt = it.publishedAt!!,
                            url = it.url!!,
                        )
                    }

                    // Save to Room database
                    articleDao.clearArticles()
                    articleDao.insertArticles(articleEntities)

                    _newsArticles.value = articles
                } else {
                    Log.e("NewsViewModel", "API Error: ${response.message()}")
                    articleDao.getAllArticles()
                }
            } catch (e: Exception) {
                Log.e("NewsViewModel", "Exception fetching news: ${e.message}", e)
                articleDao.getAllArticles()
            }
        }
    }
    fun fetchOfflineNews(){
        GlobalScope.launch {
            try {
                articleDao.getAllArticles()
                print("OFFLINE ${articleDao.getAllArticles()}")
            } catch (e : Exception){
                Log.e("NewsViewModel", "Exception fetching news: ${e.message}", e)
            }
        }
    }
}

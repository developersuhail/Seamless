package com.seamless.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit

import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private const val BASE_URL = "https://newsapi.org/v2/"

    // Create the logging interceptor g print API calls
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // Create OkHttpClient with interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

    // Create Retrofit instance
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create()) // Or GsonConverterFactory.create()
        .client(okHttpClient)
        .build()

    // Create News API service
    val newsApiService: NewsApiService = retrofit.create(NewsApiService::class.java)
}

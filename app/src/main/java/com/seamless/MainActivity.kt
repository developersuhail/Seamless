package com.seamless

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.seamless.adapter.NewsAdapter
import com.seamless.databinding.ActivityMainBinding
import com.seamless.util.NetworkConnectionLiveData
import com.seamless.util.NetworkUtil
import com.seamless.viewmodels.NewsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: NewsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(this)[NewsViewModel::class.java]

        binding.ivbtn.setOnClickListener{
            Toast.makeText(this, "New features available soon!", Toast.LENGTH_SHORT).show()
        }
        // Call Api
        if (NetworkUtil.isInternetAvailable(this)) {
            viewModel.fetchNews()
        } else{
            viewModel.fetchOfflineNews()
            Log.d("TAG", "onCreateOffline: ")
            Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()

        }

        val networkConnection = NetworkConnectionLiveData(this)
        networkConnection.observe(this) { isConnected ->
            if (isConnected) {
                viewModel.fetchNews()
                Toast.makeText(this, "Connected to the Internet", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.fetchOfflineNews()
                Log.d("TAG", "onCreateOffline: ")
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }
        }

        // Observe LiveData
        viewModel.newsArticles.observe( this, Observer { newsArticles ->
            if (newsArticles != null && newsArticles.size>0) {
                val adapter = NewsAdapter(this, newsArticles)
                binding.recyclerView.adapter = adapter
                binding.recyclerView.layoutManager = LinearLayoutManager(this)

            } else {
                Toast.makeText(this, "No articles found", Toast.LENGTH_SHORT).show()
            }
        })


    }
}

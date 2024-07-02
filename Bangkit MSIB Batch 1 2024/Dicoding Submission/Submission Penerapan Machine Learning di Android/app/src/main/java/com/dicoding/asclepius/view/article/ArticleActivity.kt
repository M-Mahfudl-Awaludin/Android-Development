package com.dicoding.asclepius.view.article

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.dicoding.asclepius.R
import com.dicoding.asclepius.databinding.ActivityArticleBinding
import com.google.android.material.snackbar.Snackbar

class ArticleActivity : AppCompatActivity() {

    private val viewModel: ArticleViewModel by viewModels()
    private lateinit var binding: ActivityArticleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityArticleBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setupObserver()
    }


    private fun setupObserver() = with(binding) {
        viewModel.newsResponse.observe(this@ArticleActivity) {
            when (it) {
                is Result.Success -> {
                    showLoading(false)
                    val newsAdapter = ArticleAdapter(it.data?.articles ?: listOf()) {
                        val intent = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse(it.url) }
                        startActivity(intent)
                    }
                    rvNews.adapter = newsAdapter
                }

                is Result.Loading -> showLoading(true)

                is Result.Error -> {
                    Snackbar.make(root, it.error, Snackbar.LENGTH_INDEFINITE).setAction(getString(R.string.understand)) { }.show()
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) = with(binding) {
        loadingBar.isVisible = isLoading
        rvNews.isVisible = !loadingBar.isVisible
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return super.onSupportNavigateUp()
    }

}
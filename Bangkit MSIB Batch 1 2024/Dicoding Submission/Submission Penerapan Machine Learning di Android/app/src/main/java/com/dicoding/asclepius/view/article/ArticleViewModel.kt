package com.dicoding.asclepius.view.article


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.response.ArticleResponse
import com.dicoding.asclepius.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ArticleViewModel : ViewModel() {

    private var _newsResponse: MutableLiveData<Result<ArticleResponse>> = MutableLiveData()
    val newsResponse: LiveData<Result<ArticleResponse>> = _newsResponse

    init {
        getListNews()
    }

    private fun getListNews() {
        viewModelScope.launch {
            val apiConfig = ApiConfig.getInstance()
            apiConfig.getArticle().enqueue(object : Callback<ArticleResponse> {
                override fun onResponse(call: Call<ArticleResponse>, response: Response<ArticleResponse>) {
                    if (response.isSuccessful) {
                        _newsResponse.value = Result.Success(response.body())
                    }
                }

                override fun onFailure(call: Call<ArticleResponse>, t: Throwable) {
                    _newsResponse.value = Result.Error(t.message.orEmpty())
                }
            })
        }
    }
}
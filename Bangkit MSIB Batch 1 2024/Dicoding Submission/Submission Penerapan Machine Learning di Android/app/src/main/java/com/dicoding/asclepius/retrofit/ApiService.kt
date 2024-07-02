package com.dicoding.asclepius.retrofit

import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("everything")
    fun getArticle(
        @Query("q") q: String = "cancer",
        @Query("language") language: String = "en",
        @Query("pageSize") pageSize: Int = 12,
        @Query("apiKey") apiKey: String = BuildConfig.KEY
    ): Call<ArticleResponse>

}
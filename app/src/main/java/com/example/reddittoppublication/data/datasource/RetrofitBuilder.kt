package com.example.reddittoppublication.data.datasource

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    val apiService: ApiService = getRetrofit().create(ApiService::class.java)
    private const val BASE_URL = "https://www.reddit.com/"

    private fun getRetrofit() = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}
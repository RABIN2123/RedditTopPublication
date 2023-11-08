package com.example.reddittoppublication.network

import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top.json?limit=10&t=day")
    suspend fun getPosts(
        @Query("after") nextPage: String?
    ): JsonData
}
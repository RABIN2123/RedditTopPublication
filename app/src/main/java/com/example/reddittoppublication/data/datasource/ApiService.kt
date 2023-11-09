package com.example.reddittoppublication.data.datasource

import com.example.reddittoppublication.data.model.TopRedditHolderResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top.json")
    suspend fun getTopRedditPosts(
        @Query("after") nextPage: String?,
        @Query("limit") limit: Int = 50,
        @Query("t") time: String = "week"
    ): TopRedditHolderResponse
}
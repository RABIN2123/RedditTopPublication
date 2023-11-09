package com.example.reddittoppublication.data.datasource

import com.example.reddittoppublication.data.model.TopRedditHolderResponse

interface ApiHelper {
    suspend fun getPosts(nextPage: String?): TopRedditHolderResponse
}
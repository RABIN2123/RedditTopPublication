package com.example.reddittoppublication.data.datasource

import com.example.reddittoppublication.data.model.TopRedditHolderResponse

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getPosts(nextPage: String?): TopRedditHolderResponse {
        return apiService.getTopRedditPosts(nextPage = nextPage)
    }
}
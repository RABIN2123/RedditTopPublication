package com.example.reddittoppublication.network

class ApiHelperImpl(private val apiService: ApiService) : ApiHelper {
    override suspend fun getPosts(nextPage: String?): JsonData {
        return apiService.getPosts(nextPage = nextPage)
    }
}
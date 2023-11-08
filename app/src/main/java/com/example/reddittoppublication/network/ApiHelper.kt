package com.example.reddittoppublication.network

interface ApiHelper {
    suspend fun getPosts(nextPage: String?): JsonData
}
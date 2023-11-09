package com.example.reddittoppublication.domain.repository

import com.example.reddittoppublication.data.datasource.ApiHelper
import com.example.reddittoppublication.domain.model.PostPage
import com.example.reddittoppublication.domain.model.toDomain

class Repository(private val apiHelper: ApiHelper) {
    suspend fun getPosts(nextPage: String?): PostPage {
        return apiHelper.getPosts(nextPage).toDomain()
    }
}

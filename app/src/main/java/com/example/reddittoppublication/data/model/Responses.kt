package com.example.reddittoppublication.data.model

import com.google.gson.JsonObject
import com.google.gson.annotations.SerializedName

data class TopRedditHolderResponse(
    @SerializedName("data")
    val data: TopRedditResponse
) {

    data class TopRedditResponse(
        @SerializedName("after")
        val nextPage: String,
        @SerializedName("children")
        val children: List<PostsHolderResponse>
    ) {

        data class PostsHolderResponse(
            @SerializedName("data")
            val data: PostResponse
        ) {

            data class PostResponse(
                @SerializedName("selftext_html")
                val content: String?,
                @SerializedName("name")
                val id: String,
                @SerializedName("author")
                val author: String,
                @SerializedName("title")
                val title: String,
                @SerializedName("num_comments")
                val numComments: Int,
                @SerializedName("created")
                val created: Long,
                @SerializedName("thumbnail")
                val thumbnail: String,
                @SerializedName("url_overridden_by_dest")
                val imgFullSize: String,
                @SerializedName("post_hint")
                val hint: String,
                @SerializedName("media_metadata")
                val mediaMetadata: JsonObject?
            )
        }
    }
}
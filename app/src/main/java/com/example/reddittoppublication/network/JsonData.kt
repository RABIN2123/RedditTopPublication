package com.example.reddittoppublication.network

import com.google.gson.annotations.SerializedName

data class JsonData(
    @SerializedName("data")
    val data: DataObject

)

data class DataObject(
    @SerializedName("after")
    val nextPage: String,
    @SerializedName("children")
    val children: List<Children>
)

data class Children(
    val data: DataChildren
)

data class DataChildren(
    @SerializedName("selftext_html")
    val content: String,
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
    val thumbnail: String
)
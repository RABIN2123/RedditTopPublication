package com.example.reddittoppublication.network

import com.google.gson.annotations.SerializedName

data class JsonData(
    @SerializedName("data")
    val data: List<Children>
)

data class Children(
    @SerializedName("after")
    val nextPage: String,
    @SerializedName("data")
    val data: List<DataChildren>
)

data class DataChildren(
    @SerializedName("author")
    val author: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("num_comments")
    val numComments: Int,
//    val date: String,
    val thumbnail: String
)
package com.example.reddittoppublication.item

data class DataList(
    val data: List<Data> = listOf(),
    val nextPage: String = ""
)

data class Data(
    val id: String = "",
    val created: String = "",
    val author: String = "",
    val title: String = "",
    val numComments: Int = 0,
    val content: String? = "",
    val thumbnail: String = ""
)
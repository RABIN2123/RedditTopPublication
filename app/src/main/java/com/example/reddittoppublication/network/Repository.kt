package com.example.reddittoppublication.network

import com.example.reddittoppublication.item.Data
import com.example.reddittoppublication.item.DataList
import kotlin.math.ceil

class Repository(private val apiHelper: ApiHelper) {
    suspend fun getPosts(nextPage: String?): DataList {
        val state = apiHelper.getPosts(nextPage)
        return DataList(
            data = state.data.children.map { item ->
                Data(
                    id = item.data.id,
                    created = convertTime(item.data.created),
                    author = item.data.author,
                    title = item.data.title,
                    content = item.data.content,
                    numComments = item.data.numComments,
                    thumbnail = item.data.thumbnail
                )
            },
            nextPage = state.data.nextPage
        )
    }

    private fun convertTime(created: Long): String {
        var result = "days time ago"
        val listWithCoef = listOf(60, 60, 24)
        var timeAgo = (System.currentTimeMillis() / 1000 - created).toFloat()
        listWithCoef.forEachIndexed { index, number ->
            if (timeAgo >= number) {
                timeAgo = ceil(timeAgo / number)
            } else {
                result = "${timeAgo.toInt()} " + when (index) {
                    0 -> "seconds ago"
                    1 -> "minutes ago"
                    2 -> "hours ago"
                    else -> "days ago"
                }
                return@forEachIndexed
            }
            result = "${timeAgo.toInt()} days ago"
        }
        return result
    }
}
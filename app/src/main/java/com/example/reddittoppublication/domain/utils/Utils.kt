package com.example.reddittoppublication.domain.utils

import com.google.gson.JsonObject
import kotlin.math.ceil

fun parseJsonToMap(jsonObj: JsonObject): String {
    val key = jsonObj[jsonObj.keySet()
        .first()].asJsonObject["p"].asJsonArray[0].asJsonObject["u"].toString()
    return key.replace("preview", "i").substringBefore("?").replace("\"", "")
}

fun convertTime(created: Long): String {
    var result = "unknown"
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
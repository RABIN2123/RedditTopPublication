package com.example.reddittoppublication.domain.model

import android.text.Html
import com.example.reddittoppublication.data.model.TopRedditHolderResponse
import com.example.reddittoppublication.domain.utils.convertTime
import com.example.reddittoppublication.domain.utils.parseJsonToMap


data class PostPage(
    val posts: List<Post> = listOf(),
    val nextPage: String = ""
) {

    data class Post(
        val id: String = "",
        val created: String = "",
        val author: String = "",
        val title: String = "",
        val numComments: Int = 0,
        val content: String? = null,
        val thumbnail: String = "",
        val img: String = "",
        val postHint: String? = ""
    )
}

fun TopRedditHolderResponse.toDomain(): PostPage {
    return PostPage(
        posts = data.children.map { item ->
            val newImage = if (item.data.mediaMetadata != null) {
                parseJsonToMap(item.data.mediaMetadata)
            } else null

            PostPage.Post(
                id = item.data.id,
                created = convertTime(item.data.created),
                author = "u/" + item.data.author,
                title = item.data.title,
                content = if (item.data.content != null) {
                    Html.fromHtml(
                        Html.fromHtml(item.data.content, Html.FROM_HTML_MODE_COMPACT)
                            .toString(),
                        Html.FROM_HTML_MODE_COMPACT
                    ).toString()
                } else {
                    ""
                },
                numComments = item.data.numComments,
                thumbnail =
                if (!item.data.imgFullSize.endsWith(".gif") && item.data.thumbnail != "image")
                    item.data.thumbnail else item.data.imgFullSize,
                img = newImage ?: item.data.imgFullSize,
                postHint = item.data.hint
            )
        }.filter { item -> item.postHint != "hosted:video" && item.postHint != "link" },
        nextPage = data.nextPage
    )
}

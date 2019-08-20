package space.foxmount.redfox.data.api

sealed class Result<out T : Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val errorCode: Int = -1, var errMsg: String = "") : Result<Nothing>()
    data class ErrorTyped<out T : Any>(val errorCode: Int, val errData: T?) : Result<T>()
}

data class RedditResponse(
    val kind: String,
    val data: ResponseData
)

data class ResponseData(
    val dist: Int,
    val after: String,
    val before: String,
    val children: List<ChildData>
)

data class ChildData(val kind: String, val data: TopicData)
data class TopicData(
    val title: String, val name: String
    , val author: String, val subreddit: String
    , val url: String, val permalink: String, val created: Float
    , val thumbnail: String, val thumbnail_width: Int, val thumbnail_height: Int
    , val post_hint: String, val selftext: String?
    , val num_comments: Int, val ups: Int
    , val media: Media?
)

data class Media(val reddit_video: RedditVideoData?)

data class RedditVideoData(
    val fallback_url: String
    , val is_gif: Boolean

)

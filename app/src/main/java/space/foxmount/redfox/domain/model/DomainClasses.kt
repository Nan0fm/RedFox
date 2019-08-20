package space.foxmount.redfox.domain.model

data class TopicList(val topic: String, val topics: List<Topic>)

data class Topic(
    val title: String, val topicType: String?, val name: String
    , val authorName: String, val subreddit: String
    , val postDate: String
    , val thumbUrl: String
    , val selfText: String?
    , val urlLink: String?
    , val videoLink: String?, val isGif: Boolean = false
    , val rating: Int, val commentsCount: Int
    , val fullLink: String
)
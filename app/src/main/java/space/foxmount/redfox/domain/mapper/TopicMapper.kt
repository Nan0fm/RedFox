package space.foxmount.redfox.domain.mapper

import space.foxmount.redfox.data.api.Media
import space.foxmount.redfox.data.api.RedditResponse
import space.foxmount.redfox.data.api.TopicData
import space.foxmount.redfox.data.db.TopicInfo
import space.foxmount.redfox.domain.model.Topic
import java.text.SimpleDateFormat
import java.util.*

class TopicMapper {

    fun convertDataToModel(redditData: RedditResponse): List<Topic> {
        return redditData.data.children.map { redditTopic ->
            convertRedditDataToTopic(redditTopic.data)
        }
    }

    fun convertDbDataToModel(dbData: List<TopicInfo>): List<Topic> {
        return dbData.map { redditTopic ->
            convertDbDataToTopic(redditTopic)
        }
    }

    fun convertRedditDataToTopic(redditTopic: TopicData): Topic {
        with(redditTopic) {
            return Topic(
                title = title, name = name
                , topicType = post_hint
                , authorName = author
                , subreddit = subreddit
                , postDate = convertDate(created)
                , thumbUrl = thumbnail
                , rating = ups
                , commentsCount = num_comments
                , fullLink = permalink
                , videoLink = media?.reddit_video?.fallback_url
                , isGif = hasGif(media)
                , urlLink = url
                , selfText = selftext
            )
        }
    }

    fun convertDbDataToTopic(redditTopic: TopicInfo): Topic {
        with(redditTopic) {
            return Topic(
                title = title, name = name
                , topicType = topicType
                , authorName = authorName
                , subreddit = subreddit
                , postDate = postDate
                , thumbUrl = thumbUrl
                , rating = rating
                , commentsCount = commentsCount
                , fullLink = fullLink
                , videoLink = videoLink
                , isGif = isGif
                , urlLink = urlLink
                , selfText = selfText

            )
        }
    }

    fun convertTopicToDbInfo(redditTopic: Topic): TopicInfo {
        with(redditTopic) {
            return TopicInfo(
                title = title, name = name
                , topicType = topicType
                , authorName = authorName
                , subreddit = subreddit
                , postDate = postDate
                , thumbUrl = thumbUrl
                , rating = rating
                , commentsCount = commentsCount
                , fullLink = fullLink
                , videoLink = videoLink
                , isGif = isGif
                , urlLink = urlLink
                , selfText = selfText

            )
        }
    }

    private fun hasGif(media: Media?): Boolean = if (media?.reddit_video != null) {
        media.reddit_video.is_gif
    } else
        false

    private fun convertDate(redditDate: Float): String {
        val updatedate = Date(redditDate.toLong() * 1000)
        val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS")
        return format.format(updatedate)
    }
}

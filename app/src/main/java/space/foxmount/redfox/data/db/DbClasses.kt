package space.foxmount.redfox.data.db

import androidx.room.*

@Database(entities = arrayOf(Topic::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

}


@Entity
data class Topic(
    @ColumnInfo(name = "title") val title: String
    , @ColumnInfo(name = "topicType") val topicType: String?
    , @ColumnInfo(name = "name") val name: String
    , @ColumnInfo(name = "authorName") val authorName: String
    , @ColumnInfo(name = "subreddit") val subreddit: String
    , @ColumnInfo(name = "postDate") val postDate: String
    , @ColumnInfo(name = "thumbUrl") val thumbUrl: String
    , @ColumnInfo(name = "selfText") val selfText: String?
    , @ColumnInfo(name = "urlLink") val urlLink: String?
    , @ColumnInfo(name = "videoLink") val videoLink: String?
    , @ColumnInfo(name = "isGif") val isGif: Boolean = false
    , @ColumnInfo(name = "rating") val rating: Int
    , @ColumnInfo(name = "commentsCount") val commentsCount: Int
    , @ColumnInfo(name = "fullLink") val fullLink: String

) {
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0
}


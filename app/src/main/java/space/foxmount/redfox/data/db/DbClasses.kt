package space.foxmount.redfox.data.db

import android.arch.persistence.room.*
import android.content.Context

@Database(entities = arrayOf(TopicInfo::class), version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun postDao(): PostDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext, AppDatabase::class.java, "red_fox"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance
        }
    }
}


@Entity
data class TopicInfo(
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


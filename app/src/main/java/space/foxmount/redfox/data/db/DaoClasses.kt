package space.foxmount.redfox.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import io.reactivex.Maybe


@Dao
interface PostDao {
    @Query("SELECT * FROM TopicInfo")
    fun getAll(): Maybe<List<TopicInfo>>

    @Query("SELECT * FROM TopicInfo WHERE uid > :lastNameId LIMIT :count")
    fun getTopics(count: String, lastNameId: Int?): Maybe<List<TopicInfo>>

    @Query("SELECT COUNT(*) FROM TopicInfo")
    fun getCount(): LiveData<Int>

    @Query("SELECT * FROM TopicInfo WHERE uid IN (:userIds)")
    fun getAllByIds(userIds: IntArray): LiveData<List<TopicInfo>>

    @Query("SELECT uid FROM TopicInfo WHERE name LIKE :name LIMIT 1")
    fun findIdByName(name: String): LiveData<Int>

    @Query("SELECT * FROM TopicInfo WHERE authorName LIKE :first LIMIT 1")
    fun findByAuthorName(first: String): LiveData<TopicInfo>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTopics(vararg topics: TopicInfo)

    @Delete
    fun delete(topic: TopicInfo)

    @Query("DELETE FROM TopicInfo")
    fun deleteAll()

    @Query("DELETE FROM TopicInfo WHERE name IN (:topicsIds)")
    fun deleteByName(topicsIds: List<String>)

    @Delete
    fun deleteAll(users: List<TopicInfo>)
}
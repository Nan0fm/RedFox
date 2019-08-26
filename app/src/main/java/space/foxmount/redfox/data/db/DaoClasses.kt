package space.foxmount.redfox.data.db

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface PostDao {
    @Query("SELECT * FROM Topic")
    fun getAll(): LiveData<List<Topic>>

    @Query("SELECT * FROM Topic WHERE uid > :lastNameId LIMIT :count")
    fun getTopics(count: String, lastNameId: Int?): LiveData<List<Topic>>

     @Query("SELECT * FROM Topic WHERE uid > (SELECT uid FROM Topic WHERE name LIKE :lastName LIMIT 1 ) LIMIT :count")
//    FROM   table1 [, table2 ]
//    WHERE  column_name OPERATOR
//    (SELECT column_name [, column_name ]
//    FROM table1 [, table2 ]
//    [WHERE])
    fun getTopics(count: String, lastName: String?): LiveData<List<Topic>>

    @Query("SELECT COUNT(*) FROM Topic")
    fun getCount(): LiveData<Int>

    @Query("SELECT * FROM Topic WHERE uid IN (:userIds)")
    fun getAllByIds(userIds: IntArray): LiveData<List<Topic>>

    @Query("SELECT uid FROM Topic WHERE name LIKE :name LIMIT 1")
    fun findIdByName(name: String): LiveData<Int>

    @Query("SELECT * FROM Topic WHERE authorName LIKE :first LIMIT 1")
    fun findByAuthorName(first: String): LiveData<Topic>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveTopic( topic: Topic)

    @Delete
    fun delete(topic: Topic)

    @Query("DELETE FROM Topic")
    fun deleteAll()

    @Query("DELETE FROM Topic WHERE name IN (:topicsIds)")
    fun deleteByName(topicsIds: List<String>)

    @Delete
    fun deleteAll(users: List<Topic>)
}
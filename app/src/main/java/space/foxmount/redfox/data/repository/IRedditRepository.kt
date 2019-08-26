package space.foxmount.redfox.data.repository

import space.foxmount.redfox.data.db.Topic

val QUERY_LIMIT = "limit"
val QUERY_AFTER = "after"
val QUERY_COUNT = "10"

interface IRedditRepository {

    fun saveAll(list: List<Topic>)

    fun removeAll()


}
package space.foxmount.redfox.data.repository

import space.foxmount.redfox.data.db.Topic

const val QUERY_LIMIT = "limit"
const val QUERY_AFTER = "after"
const val QUERY_COUNT = "10"

interface IRedditRepository {

    fun saveAll(list: List<Topic>)

    fun removeAll()


}
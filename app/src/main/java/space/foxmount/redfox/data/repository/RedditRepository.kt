package space.foxmount.redfox.data.repository

import androidx.lifecycle.LiveData
import space.foxmount.redfox.data.api.ApiRepository
import space.foxmount.redfox.data.api.RedditResponse
import space.foxmount.redfox.data.db.PostDao
import space.foxmount.redfox.data.db.Topic
import space.foxmount.redfox.domain.mapper.TopicMapper
import javax.inject.Inject


class RedditRepository @Inject constructor(val api: ApiRepository, val topicsDao: PostDao) :
    IRedditRepository {

    override fun saveAll(list: List<Topic>) {
        list.forEach { topicsDao.saveTopic(it) }
    }

    override fun removeAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    fun getTopics(count: Int, lastName: String?): LiveData<List<Topic>> {
//        return topicsDao.getTopics(count.toString(), topicsDao.findIdByName(lastName))
//    }

    fun getTopics(): LiveData<List<Topic>> {
        return topicsDao.getAll()
    }

    fun getTopics(count: Int, lastName: String?): LiveData<List<Topic>> {
        return topicsDao.getTopics(count.toString(), lastName)
    }

    fun save(response: RedditResponse) {
        TopicMapper().convertDataToModel(response).forEach {
            topicsDao.saveTopic(it)
        }

    }

}
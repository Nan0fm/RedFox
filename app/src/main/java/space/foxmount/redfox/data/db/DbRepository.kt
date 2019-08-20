package space.foxmount.redfox.data.db

import android.content.Context
import io.reactivex.Completable
import io.reactivex.Observable
import space.foxmount.redfox.data.repository.*
import space.foxmount.redfox.domain.mapper.TopicMapper
import space.foxmount.redfox.domain.model.Topic

class DbRepository(appContext: Context) : SimpleRepository() {

    val dao: PostDao

    init {
        dao = AppDatabase.getInstance(appContext)!!.postDao()
    }

    override fun getAll(query: QueryTopic): Observable<List<Topic>> {
        return dao.getTopics(
            query.get(QUERY_LIMIT) ?: QUERY_COUNT,
            dao.findIdByName(query.get((QUERY_AFTER)) ?: "").value
        )
            .map { t -> TopicMapper().convertDbDataToModel(t) }.toObservable()
    }

    override fun getAll(): Observable<List<Topic>> {
        return dao.getAll().map { t -> TopicMapper().convertDbDataToModel(t) }.toObservable()
    }

    override fun saveAll(list: List<Topic>): Observable<List<Topic>> {
        return Completable.fromCallable {
            list.map { t -> dao.saveTopics(TopicMapper().convertTopicToDbInfo(t)) }
        }.andThen(Observable.just(list))
    }

    override fun removeAll(): Completable {
        return Completable.fromCallable { dao.deleteAll() }
    }

    override fun removeAll(list: List<Topic>): Completable {
        return Completable.fromCallable {
            dao.deleteByName(list.map { t -> t.name }.toList())
        }
    }
}
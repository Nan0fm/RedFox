package space.foxmount.redfox.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import space.foxmount.redfox.RedFoxApp.Companion.isNetworkAvailable
import space.foxmount.redfox.data.api.ApiRepository
import space.foxmount.redfox.data.db.DbRepository
import space.foxmount.redfox.domain.model.Topic


class RedditRepository(val topicsApi: ApiRepository, val topicDb: DbRepository) :
    IRedditRepository {


    fun getAll(count: Int, lastName: String?): Observable<List<Topic>> {
        return getAll(
            QueryTopic(this)
                .where(QUERY_LIMIT, count.toString())
                .where(QUERY_AFTER, lastName ?: "")
        )
    }

    override fun getAll(query: QueryTopic): Observable<List<Topic>> {
        return Observable.concatArrayEager(
            topicDb.getAll(query).subscribeOn(Schedulers.io()),
            Observable.defer {
                if (isNetworkAvailable())
                    topicsApi.getAll(query).subscribeOn(Schedulers.io())
                        .flatMap { l ->
                            topicDb.getAll(query)
                                .flatMapCompletable { old -> topicDb.removeAll(old) }
                                .andThen(topicDb.saveAll(l))
                        }
                else
                    Observable.empty()
            }.subscribeOn(Schedulers.io())
        )
    }

    override fun getAll(): Observable<List<Topic>> {
        return Observable.concatArrayEager(
            topicDb.getAll().subscribeOn(Schedulers.io()),
            Observable.defer {
                if (isNetworkAvailable())
                    topicsApi.getAll().subscribeOn(Schedulers.io())
                        .flatMap { l ->
                            topicDb.removeAll()
                            topicDb.saveAll(l)
                        }
                else
                    Observable.empty()
            }.subscribeOn(Schedulers.io())
        )
    }

    override fun saveAll(list: List<Topic>): Observable<List<Topic>> {
        return Observable.defer {
            if (isNetworkAvailable())
                topicDb.saveAll(list).subscribeOn(Schedulers.io())
            else
                Observable.error(IllegalAccessError("Some error occured"))
        }
    }

    override fun removeAll(): Completable {
        return Completable.defer {
            if (isNetworkAvailable())
                topicDb.removeAll().subscribeOn(Schedulers.io())
            else
                Completable.error(IllegalAccessError("Some error occured"))
        }
    }

    override fun removeAll(list: List<Topic>): Completable {
        return Completable.defer {
            if (isNetworkAvailable())
                topicDb.removeAll(list).subscribeOn(Schedulers.io())
            else
                Completable.error(IllegalAccessError("Some error occured"))
        }
    }

}
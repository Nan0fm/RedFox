package space.foxmount.redfox.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import space.foxmount.redfox.domain.model.Topic

abstract class SimpleRepository : IRedditRepository {

    override fun getAll(): Observable<List<Topic>> {
        return Observable.empty()
    }

    override fun getAll(query: QueryTopic): Observable<List<Topic>> {
        return Observable.empty()
    }

    override fun saveAll(list: List<Topic>): Observable<List<Topic>> {
        return Observable.empty()
    }

    override fun removeAll(): Completable {
        return Completable.complete()
    }

    override fun removeAll(list: List<Topic>): Completable {
        return Completable.complete()
    }
}
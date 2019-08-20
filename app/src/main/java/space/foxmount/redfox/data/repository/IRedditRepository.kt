package space.foxmount.redfox.data.repository

import io.reactivex.Completable
import io.reactivex.Observable
import space.foxmount.redfox.domain.model.Topic

val QUERY_LIMIT = "limit"
val QUERY_AFTER = "after"
val QUERY_COUNT = "10"

interface IRedditRepository {

    fun getAll(): Observable<List<Topic>>

    fun getAll(query: QueryTopic): Observable<List<Topic>>


    fun saveAll(list: List<Topic>): Observable<List<Topic>>

    fun removeAll(): Completable

    fun removeAll(list: List<Topic>): Completable
}
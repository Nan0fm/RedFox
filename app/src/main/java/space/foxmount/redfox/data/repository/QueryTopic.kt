package space.foxmount.redfox.data.repository

import io.reactivex.Observable
import space.foxmount.redfox.domain.model.Topic

class QueryTopic constructor(private val dataSource: IRedditRepository) {

    val params: MutableMap<String, String> = mutableMapOf()

    fun has(property: String): Boolean {
        return params[property] != null
    }

    fun get(property: String): String? {
        return params[property]
    }

    fun where(property: String, value: String): QueryTopic {
        params[property] = value
        return this
    }

    fun findAll(): Observable<List<Topic>> {
        return dataSource.getAll(this)
    }

    fun findFirst(): Observable<Topic> {
        return dataSource.getAll(this)
            .filter { it.isNotEmpty() }
            .map { it.first() }
    }
}
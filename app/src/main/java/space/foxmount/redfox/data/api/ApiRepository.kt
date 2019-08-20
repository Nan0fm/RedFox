package space.foxmount.redfox.data.api

import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import retrofit2.Response
import space.foxmount.redfox.data.repository.*
import space.foxmount.redfox.domain.mapper.TopicMapper
import space.foxmount.redfox.domain.model.Topic
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

class ApiRepository(val api: RedditRequest) : SimpleRepository() {

    private val topics = PublishSubject.create<List<Topic>>()

    override fun getAll(query: QueryTopic): Observable<List<Topic>> {
        return when {
            query.has(QUERY_LIMIT) -> api.getListTopTopics(
                generateParams(
                    query.get(QUERY_LIMIT),
                    query.get((QUERY_AFTER))
                )
            ).flatMap { r ->
                Observable.just(TopicMapper().convertDataToModel(r))
            }
            else -> throw(IllegalArgumentException("Unsupported query $query for PostEntity"))
        }
    }

    suspend fun <T : Any> safetyCall(
        call: suspend () -> Response<T>,
        requestedPage: Int
    ): Result<Any>? {
        var response: Result<Any>?
        try {
            val result: Response<T> = call.invoke()
            if (result.isSuccessful) response = Result.Success<T>(result.body()!!)
            else response = parseError(requestedPage, result)
        } catch (e: Exception) {
            response = Result.Error(errMsg = e.message ?: "")
            when (e) {
                is SocketTimeoutException -> response = Result.Error(ERROR_TIMEOUT)
                is UnknownHostException -> response = Result.Error(ERROR_NO_INTERNET)
            }
        }
        return response
    }

    fun generateParams(count: String? = QUERY_COUNT, lastName: String?): Map<String, String> {
        val fieldsMap = HashMap<String, String>()
        lastName?.let { fieldsMap["after"] = lastName }
        fieldsMap["limit"] = count.toString()
        return fieldsMap
    }
}
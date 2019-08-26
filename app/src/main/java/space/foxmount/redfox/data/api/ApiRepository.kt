package space.foxmount.redfox.data.api

import retrofit2.Response
import space.foxmount.redfox.data.repository.QUERY_COUNT
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*

class ApiRepository {

    val apiInterface = ApiFactory.API_SERVICE

    companion object {

        private var repo: ApiRepository? = null

        fun getInstance(): ApiRepository {
            if (repo == null) {
                repo = ApiRepository()
            }
            return repo as ApiRepository
        }
    }

    suspend fun getTopics(count: String = QUERY_COUNT, lastName: String?): Result<Any>? {
        val call: suspend () -> Response<RedditResponse> =
            { apiInterface.getListTopTopics(generateParams(count, lastName)) }
        return safetyCall(call, PAGE_COMMON)
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

    fun generateParams(count: String, lastName: String?): Map<String, String> {
        val fieldsMap = HashMap<String, String>()
        lastName?.let { fieldsMap["after"] = lastName }
        fieldsMap["limit"] = count.toString()
        return fieldsMap
    }
}
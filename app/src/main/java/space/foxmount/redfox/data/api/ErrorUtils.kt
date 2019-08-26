package space.foxmount.redfox.data.api

import com.github.ajalt.timberkt.Timber
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response
import java.io.IOException

val RESPONSE_OK = 200
val RESPONSE_VALUE_ERROR = 400
val RESPONSE_AUTH_ERROR = 401
val RESPONSE_NOT_FOUND_ERROR = 404
val ERROR_WRONG_METHOD = 405
val ERROR_TIMEOUT = 408
val ERROR_NO_INTERNET = 499
val RESPONSE_SERVER_ERROR = 500
val RESPONSE_SERVER_NOT_WORKING = 503

val PAGE_COMMON = 10

data class ErrorModel(val errorMessage: String)

fun parseError(requestPage: Int, response: Response<*>): Result<Any> {
    val code = response.code()
    var error: Result<Any> = Result.Error(code, "Some Error")
    when (code) {
        RESPONSE_VALUE_ERROR -> {
            var c: Class<*> = ErrorModel::class.java
            when (requestPage) {
                PAGE_COMMON -> c = ErrorModel::class.java
            }
            error = Result.ErrorTyped(code, parseError(c, response))
        }
        RESPONSE_NOT_FOUND_ERROR,
        ERROR_WRONG_METHOD,
        RESPONSE_SERVER_ERROR,
        ERROR_TIMEOUT,
        RESPONSE_SERVER_NOT_WORKING -> {
            error = Result.Error(code, response.errorBody()?.string() ?: "")
//            timber.log.Timber.e("Server error", error as Result.Error)
        }
        else -> {
            error = Result.Error(code, response.errorBody()?.string() ?: "")
        }
    }
    return error
}

fun <T, E> parseError(c: Class<T>, response: Response<E>): T? {
    val converter: Converter<ResponseBody, T> = ApiFactory.retrofit()
        .responseBodyConverter(c, arrayOfNulls<Annotation>(0))
    var error: T? = null
    try {
        error = converter.convert(response.errorBody()!!)
    } catch (e: IOException) {
        Timber.e(e)
    }
    return error
}


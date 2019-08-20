package space.foxmount.redfox.data.api

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface RedditRequest {

    @GET("/top/.json")
    fun getListTopTopics(): Observable<RedditResponse>

    @GET("/top/.json")
    fun getListTopTopics(@QueryMap(encoded = true) paramsMap: Map<String, String>): Observable<RedditResponse>

}
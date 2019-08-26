package space.foxmount.redfox.data.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap


interface RedditRequest {

    @GET("/top/.json")
    suspend  fun getListTopTopics(): Response<RedditResponse>

    @GET("/top/.json")
    suspend  fun getListTopTopics(@QueryMap(encoded = true) paramsMap: Map<String, String>): Response<RedditResponse>

}
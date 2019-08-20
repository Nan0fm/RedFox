package space.foxmount.redfox.data.api

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


class RedditService {

    private val REDDIT_URL = "https://www.reddit.com"


    private var mRetrofit: Retrofit? = null

    companion object {
        private var mInstance: RedditService? = null

        fun getInstance(): RedditService? {
            if (mInstance == null) {
                mInstance =
                    RedditService()
            }
            return mInstance
        }
    }

    init {
        val gson = GsonBuilder()
            .setLenient()
            .create()
        mRetrofit = Retrofit.Builder()
            .baseUrl(REDDIT_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }


    fun getApiData(): RedditRequest {
        return mRetrofit!!.create(RedditRequest::class.java)
    }

}
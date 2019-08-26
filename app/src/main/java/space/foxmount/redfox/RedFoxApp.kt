package space.foxmount.redfox

import android.app.Application
import space.foxmount.redfox.di.*

class RedFoxApp : Application() {

    lateinit var component: RedditComponent


    override fun onCreate() {
        super.onCreate()
        component =
            DaggerRedditComponent.builder()
                .appModule(AppModule(this))
                .contextModule(ContextModule(applicationContext))
                .preferenceModule(PreferenceModule())
                .apiModule(ApiModule())
                .roomModule(RoomModule(this))
                .build()
    }


}
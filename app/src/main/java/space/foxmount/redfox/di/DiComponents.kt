package space.foxmount.redfox.di

import android.app.Application
import dagger.Component
import space.foxmount.redfox.data.api.ApiRepository
import space.foxmount.redfox.data.db.AppDatabase
import space.foxmount.redfox.data.db.PostDao
import space.foxmount.redfox.data.repository.RedditRepository
import space.foxmount.redfox.ui.RedditActivity
import space.foxmount.redfox.ui.topics.TopicsFragment
import space.foxmount.redfox.ui.topics.TopicsViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, PreferenceModule::class, ContextModule::class, ApiModule::class, RoomModule::class])
interface RedditComponent {

    fun inject(activity: RedditActivity)
    fun inject(fragment: TopicsFragment)
    fun inject(vm: TopicsViewModel)

    fun userDao(): PostDao

    fun demoDatabase(): AppDatabase

    fun apiRepository(): ApiRepository
    fun quizRepository(): RedditRepository

    fun application(): Application

}

